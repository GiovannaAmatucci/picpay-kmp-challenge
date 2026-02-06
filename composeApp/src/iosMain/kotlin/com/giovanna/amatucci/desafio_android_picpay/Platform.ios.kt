package com.giovanna.amatucci.desafio_android_picpay

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val showRefreshButton: Boolean = false
    override val supportsPullToRefresh: Boolean = true
}

actual fun getPlatform(): Platform = IOSPlatform()