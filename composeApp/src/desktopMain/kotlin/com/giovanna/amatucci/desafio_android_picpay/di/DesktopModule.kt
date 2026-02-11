package com.giovanna.amatucci.desafio_android_picpay.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.giovanna.amatucci.desafio_android_picpay.AppConfig
import com.giovanna.amatucci.desafio_android_picpay.data.local.db.AppDatabase
import com.giovanna.amatucci.desafio_android_picpay.data.local.getDatabaseBuilder
import com.giovanna.amatucci.desafio_android_picpay.data.remote.network.HttpClientConfig
import com.giovanna.amatucci.desafio_android_picpay.util.ConnectivityObserver
import com.giovanna.amatucci.desafio_android_picpay.util.CryptoManager
import com.giovanna.amatucci.desafio_android_picpay.util.DesktopConnectivityObserver
import com.giovanna.amatucci.desafio_android_picpay.util.DesktopCryptoManager
import com.giovanna.amatucci.desafio_android_picpay.util.LogWriter
import org.koin.dsl.module

val desktopModule = module {
    single {
        HttpClientConfig(
            baseUrl = AppConfig.BASE_URL,
            debug = AppConfig.DEBUG_MODE,
            requestTimeout = AppConfig.REQUEST_TIMEOUT,
            connectTimeout = AppConfig.CONNECT_TIMEOUT
        )
    }
    single<AppDatabase> {
        getDatabaseBuilder().setDriver(BundledSQLiteDriver()).build()
    }

    single<CryptoManager> { DesktopCryptoManager() }

    single<LogWriter> {
        object : LogWriter {
            override fun d(tag: String, message: String) = println("DEBUG [$tag]: $message")
            override fun w(tag: String, message: String, t: Throwable?) {
                println("WARN [$tag]: $message")
                t?.printStackTrace()
            }
            override fun e(tag: String, message: String, t: Throwable?) {
                System.err.println("ERROR [$tag]: $message")
                t?.printStackTrace()
            }
        }
    }
    single<ConnectivityObserver> {
        DesktopConnectivityObserver()
    }
}