package com.giovanna.amatucci.desafio_android_picpay.util


class IosCryptoManager : CryptoManager {
    override fun encrypt(bytes: ByteArray): ByteArray {
        return bytes
    }

    override fun decrypt(bytes: ByteArray): ByteArray {
        return bytes
    }
}