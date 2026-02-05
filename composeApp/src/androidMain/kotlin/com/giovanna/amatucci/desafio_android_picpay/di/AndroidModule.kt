package com.giovanna.amatucci.desafio_android_picpay.di

import androidx.room.Room
import com.giovanna.amatucci.desafio_android_picpay.AppConfig
import com.giovanna.amatucci.desafio_android_picpay.data.local.db.AppDatabase
import com.giovanna.amatucci.desafio_android_picpay.data.remote.network.HttpClientConfig
import com.giovanna.amatucci.desafio_android_picpay.util.AndroidLogWriter
import com.giovanna.amatucci.desafio_android_picpay.util.ConnectivityObserver
import com.giovanna.amatucci.desafio_android_picpay.util.CryptoManager
import com.giovanna.amatucci.desafio_android_picpay.util.LogWriter
import com.giovanna.amatucci.desafio_android_picpay.util.NetworkConnectivityObserver
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.SecureRandom

val androidModule = module {
    single {
        HttpClientConfig(
            baseUrl = AppConfig.BASE_URL,
            debug =  AppConfig.DEBUG_MODE,
            requestTimeout = AppConfig.REQUEST_TIMEOUT,
            connectTimeout = AppConfig.CONNECT_TIMEOUT
        )
    }
    single<LogWriter> { AndroidLogWriter() }
    single { CryptoManager() }
    single<AppDatabase> {
        val context = androidContext()
        val cryptoManager = get<CryptoManager>()
        val keyFile = File(context.filesDir, "safe_db_key.bin")

        val passphrase: ByteArray = if (keyFile.exists()) {
            try {
                cryptoManager.decrypt(FileInputStream(keyFile))
            } catch (e: Exception) {
                keyFile.delete()
                generateAndSaveKey(cryptoManager, keyFile)
            }
        } else {
            generateAndSaveKey(cryptoManager, keyFile)
        }

        val factory = SupportFactory(passphrase)
        Room.databaseBuilder<AppDatabase>(
            context = context,
            name = AppConfig.DATABASE_NAME
        )
            .openHelperFactory(factory)
            .fallbackToDestructiveMigration(false)
            .build()
    }

    single<ConnectivityObserver> {
        NetworkConnectivityObserver(androidContext())
    }
}
private fun generateAndSaveKey(cryptoManager: CryptoManager, file: File): ByteArray {
    val randomKey = ByteArray(32)
    SecureRandom().nextBytes(randomKey)
    cryptoManager.encrypt(randomKey, FileOutputStream(file))
    return randomKey
}