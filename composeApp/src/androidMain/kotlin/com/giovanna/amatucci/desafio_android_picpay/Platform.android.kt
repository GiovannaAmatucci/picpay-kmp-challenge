package com.giovanna.amatucci.desafio_android_picpay

import android.os.Build
class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val showRefreshButton: Boolean = false
    override val supportsPullToRefresh: Boolean = true
}

actual fun getPlatform(): Platform = AndroidPlatform()