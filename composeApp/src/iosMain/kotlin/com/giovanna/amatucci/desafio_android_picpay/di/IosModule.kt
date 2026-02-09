package com.giovanna.amatucci.desafio_android_picpay.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.giovanna.amatucci.desafio_android_picpay.AppConfig
import com.giovanna.amatucci.desafio_android_picpay.data.local.db.AppDatabase
import com.giovanna.amatucci.desafio_android_picpay.data.local.db.getDatabaseBuilder
import com.giovanna.amatucci.desafio_android_picpay.data.remote.network.HttpClientConfig
import com.giovanna.amatucci.desafio_android_picpay.util.ConnectivityObserver
import com.giovanna.amatucci.desafio_android_picpay.util.IosConnectivityObserver
import com.giovanna.amatucci.desafio_android_picpay.util.IosLogWriter
import com.giovanna.amatucci.desafio_android_picpay.util.KeychainHelper
import com.giovanna.amatucci.desafio_android_picpay.util.LogWriter
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.usePinned
import org.koin.dsl.module
import platform.Security.SecRandomCopyBytes
import platform.Security.kSecRandomDefault

@OptIn(ExperimentalForeignApi::class)
val iosModule = module {
    single {
        HttpClientConfig(
            baseUrl = AppConfig.BASE_URL,
            debug = AppConfig.DEBUG_MODE,
            requestTimeout = AppConfig.REQUEST_TIMEOUT,
            connectTimeout = AppConfig.CONNECT_TIMEOUT
        )
    }
    single<LogWriter> { IosLogWriter() }
    single<ConnectivityObserver> { IosConnectivityObserver() }
    single { KeychainHelper() }
    single<AppDatabase> {
        val keychain = get<KeychainHelper>()
        var keyBytes = keychain.getKey()
        if (keyBytes == null) {
            val newKey = generateSecureRandomKey()
            keychain.saveKey(newKey)
            keyBytes = newKey
            println("IOS: Nova chave de criptografia gerada e salva no Keychain.")
        } else {
            println("IOS: Chave de criptografia recuperada do Keychain com sucesso.")
        }
        getDatabaseBuilder().setDriver(BundledSQLiteDriver()).build()
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun generateSecureRandomKey(): ByteArray {
    val size = 32
    val key = ByteArray(size)
    key.usePinned { pinned ->
        SecRandomCopyBytes(kSecRandomDefault, size.convert(), pinned.addressOf(0))
    }
    return key
}