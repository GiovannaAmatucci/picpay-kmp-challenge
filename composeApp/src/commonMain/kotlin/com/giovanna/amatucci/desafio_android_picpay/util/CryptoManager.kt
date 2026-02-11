package com.giovanna.amatucci.desafio_android_picpay.util

interface CryptoManager {
    fun encrypt(bytes: ByteArray): ByteArray
    fun decrypt(bytes: ByteArray): ByteArray
}