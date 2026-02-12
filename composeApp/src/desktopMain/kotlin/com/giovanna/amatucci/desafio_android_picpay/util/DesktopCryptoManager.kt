package com.giovanna.amatucci.desafio_android_picpay.util

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
class DesktopCryptoManager : CryptoManager {
    private val keyStoreFile = File(System.getProperty("user.home"), KEY_STORE_FILE_NAME)

    private val keyStore: KeyStore = KeyStore.getInstance(KEY_STORE_TYPE).apply {
        if (keyStoreFile.exists()) {
            FileInputStream(keyStoreFile).use { stream ->
                load(stream, KEY_STORE_PASSWORD)
            }
        } else {
            load(null, KEY_STORE_PASSWORD)
        }
    }
    override fun encrypt(bytes: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getKey())

        val encryptedBytes = cipher.doFinal(bytes)
        val iv = cipher.iv

        ByteArrayOutputStream().use { outputStream ->
            DataOutputStream(outputStream).use { dataOutputStream ->
                dataOutputStream.writeInt(iv.size)
                dataOutputStream.write(iv)
                dataOutputStream.writeInt(encryptedBytes.size)
                dataOutputStream.write(encryptedBytes)
            }
            return outputStream.toByteArray()
        }
    }
    override fun decrypt(bytes: ByteArray): ByteArray {
        ByteArrayInputStream(bytes).use { inputStream ->
            DataInputStream(inputStream).use { dataInputStream ->
                val ivSize = dataInputStream.readInt()
                val iv = ByteArray(ivSize)
                dataInputStream.readFully(iv)

                val encryptedSize = dataInputStream.readInt()
                val encryptedBytes = ByteArray(encryptedSize)
                dataInputStream.readFully(encryptedBytes)

                val cipher = Cipher.getInstance(TRANSFORMATION)
                val spec = GCMParameterSpec(128, iv)
                cipher.init(Cipher.DECRYPT_MODE, getKey(), spec)

                return cipher.doFinal(encryptedBytes)
            }
        }
    }
    private fun getKey(): SecretKey {
        return try {
            keyStore.getKey(KEY_ALIAS, KEY_STORE_PASSWORD) as? SecretKey ?: createKey()
        } catch (e: Exception) {
            createKey()
        }
    }
    private fun createKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(ALGORITHM)
        keyGenerator.init(256)
        val key = keyGenerator.generateKey()

        keyStore.setKeyEntry(KEY_ALIAS, key, KEY_STORE_PASSWORD, null)
        FileOutputStream(keyStoreFile).use { fos ->
            keyStore.store(fos, KEY_STORE_PASSWORD)
        }

        return key
    }
    companion object {
        private const val ALGORITHM = "AES"
        private const val BLOCK_MODE = "GCM"
        private const val PADDING = "NoPadding"
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
        private const val KEY_ALIAS = "picpay_desktop_key"
        private const val KEY_STORE_TYPE = "PKCS12"
        private const val KEY_STORE_FILE_NAME = ".picpay_keystore.p12"
        private val KEY_STORE_PASSWORD = "changeit".toCharArray()
    }
}