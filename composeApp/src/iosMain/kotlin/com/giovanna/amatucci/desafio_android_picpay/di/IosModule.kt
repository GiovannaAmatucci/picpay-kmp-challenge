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
            println("IOS DATABASE: Nenhuma chave encontrada. Gerando nova chave segura...")
            keyBytes = generateSecureRandomKey()
            keychain.saveKey(keyBytes)
        } else {
            println("IOS DATABASE: Chave recuperada do Keychain com sucesso.")
        }

        val builder = getDatabaseBuilder()
        builder.setDriver(BundledSQLiteDriver())
        builder.build()
    }
}
@OptIn(ExperimentalForeignApi::class)
private fun generateSecureRandomKey(): ByteArray {
    val size = 32
    val key = ByteArray(size)
    val status = key.usePinned { pinned ->
        SecRandomCopyBytes(kSecRandomDefault, size.convert(), pinned.addressOf(0))
    }

    if (status != 0) {
        throw IllegalStateException("Falha crítica ao gerar aleatoriedade segura no iOS via SecRandomCopyBytes")
    }

    return key
}