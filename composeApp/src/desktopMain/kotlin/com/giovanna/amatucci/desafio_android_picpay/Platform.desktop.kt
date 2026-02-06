package com.giovanna.amatucci.desafio_android_picpay

class DesktopPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val showRefreshButton: Boolean = true
    override val supportsPullToRefresh: Boolean = false
}

actual fun getPlatform(): Platform = DesktopPlatform()