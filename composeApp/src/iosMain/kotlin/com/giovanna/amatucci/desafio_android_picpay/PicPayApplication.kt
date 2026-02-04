package com.giovanna.amatucci.desafio_android_picpay

import com.giovanna.amatucci.desafio_android_picpay.di.initKoin
import com.giovanna.amatucci.desafio_android_picpay.di.iosModule
import com.giovanna.amatucci.desafio_android_picpay.di.sharedModules

fun doInitKoin() {
    initKoin {
        modules(iosModule)
    }
}