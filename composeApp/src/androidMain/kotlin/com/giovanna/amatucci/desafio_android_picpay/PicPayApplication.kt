package com.giovanna.amatucci.desafio_android_picpay

import android.app.Application
import com.giovanna.amatucci.desafio_android_picpay.di.androidModule
import com.giovanna.amatucci.desafio_android_picpay.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class PicPayApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@PicPayApplication)
            modules(androidModule)
        }
    }
}
