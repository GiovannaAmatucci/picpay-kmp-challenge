package com.giovanna.amatucci.desafio_android_picpay

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform