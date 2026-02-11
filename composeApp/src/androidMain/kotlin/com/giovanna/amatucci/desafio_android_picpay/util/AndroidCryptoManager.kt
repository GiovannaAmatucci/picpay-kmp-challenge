package com.giovanna.amatucci.desafio_android_picpay.util

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class AndroidCryptoManager : CryptoManager {
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }
    private val encryptCipher
        get() = Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, getKey())
        }
    private fun getDecryptCipherForIv(iv: ByteArray): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, getKey(), GCMParameterSpec(128, iv))
        }
    }
    private fun getKey(): SecretKey {
        val existingKey = keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }
    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM, "AndroidKeyStore").apply {
            init(
                KeyGenParameterSpec.Builder(
                    KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                ).setBlockModes(BLOCK_MODE).setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false).setRandomizedEncryptionRequired(true)
                    .build()
            )
        }.generateKey()
    }
    override fun encrypt(bytes: ByteArray): ByteArray {
        val cipher = encryptCipher
        val encryptedBytes = cipher.doFinal(bytes)

        val outputStream = ByteArrayOutputStream()
        val dataOutputStream = DataOutputStream(outputStream)

        dataOutputStream.writeInt(cipher.iv.size)
        dataOutputStream.write(cipher.iv)
        dataOutputStream.writeInt(encryptedBytes.size)
        dataOutputStream.write(encryptedBytes)

        return outputStream.toByteArray()
    }

    override fun decrypt(bytes: ByteArray): ByteArray {
        val inputStream = ByteArrayInputStream(bytes)
        val dataInputStream = DataInputStream(inputStream)

        val ivSize = dataInputStream.readInt()
        val iv = ByteArray(ivSize)
        dataInputStream.readFully(iv)

        val encryptedSize = dataInputStream.readInt()
        val encryptedBytes = ByteArray(encryptedSize)
        dataInputStream.readFully(encryptedBytes)

        val cipher = getDecryptCipherForIv(iv)
        return cipher.doFinal(encryptedBytes)
    }
    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
        private const val KEY_ALIAS = "picpay_db_key"
    }
}