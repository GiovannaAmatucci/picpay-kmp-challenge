package com.giovanna.amatucci.desafio_android_picpay

interface Platform {
    val name: String
    val showRefreshButton: Boolean
    val supportsPullToRefresh: Boolean
}

expect fun getPlatform(): Platform