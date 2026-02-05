package com.giovanna.amatucci.desafio_android_picpay.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.giovanna.amatucci.desafio_android_picpay.AppConfig
import com.giovanna.amatucci.desafio_android_picpay.data.local.db.AppDatabase
import com.giovanna.amatucci.desafio_android_picpay.data.local.db.getDatabaseBuilder
import com.giovanna.amatucci.desafio_android_picpay.data.remote.network.HttpClientConfig
import com.giovanna.amatucci.desafio_android_picpay.util.ConnectivityObserver
import com.giovanna.amatucci.desafio_android_picpay.util.IosLogWriter
import com.giovanna.amatucci.desafio_android_picpay.util.LogWriter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.dsl.module

val iosModule = module {
    single { HttpClientConfig(
            baseUrl = AppConfig.BASE_URL,
            debug = AppConfig.DEBUG_MODE,
            requestTimeout = AppConfig.REQUEST_TIMEOUT,
            connectTimeout = AppConfig.CONNECT_TIMEOUT
        )
    }
    single<AppDatabase> {
        getDatabaseBuilder().setDriver(BundledSQLiteDriver()).build()
    }

    single<LogWriter> { IosLogWriter() }

    single<ConnectivityObserver> {
        object : ConnectivityObserver {
            override fun observe(): Flow<ConnectivityObserver.Status> {
                return flowOf(ConnectivityObserver.Status.Available)
            }
        }
    }
}