package com.giovanna.amatucci.desafio_android_picpay.util

class FakeCryptoManager : CryptoManager {
    override fun encrypt(bytes: ByteArray): ByteArray = bytes
    override fun decrypt(bytes: ByteArray): ByteArray = bytes
}