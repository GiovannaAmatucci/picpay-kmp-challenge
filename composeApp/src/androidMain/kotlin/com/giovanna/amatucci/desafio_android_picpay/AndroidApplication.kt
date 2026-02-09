package com.giovanna.amatucci.desafio_android_picpay

import android.app.Application
import com.giovanna.amatucci.desafio_android_picpay.di.androidModule
import com.giovanna.amatucci.desafio_android_picpay.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import timber.log.Timber

class AndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (AppConfig.DEBUG_MODE) {
            Timber.plant(Timber.DebugTree())
        }

        initKoin {
            androidLogger()
            androidContext(this@AndroidApplication)
            modules(androidModule)
        }
    }
}
