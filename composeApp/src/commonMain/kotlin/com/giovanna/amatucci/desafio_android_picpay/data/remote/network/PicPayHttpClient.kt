package com.giovanna.amatucci.desafio_android_picpay.data.remote.network

import com.giovanna.amatucci.desafio_android_picpay.util.LogWriter
import com.giovanna.amatucci.desafio_android_picpay.util.TAG
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
data class HttpClientConfig(
    val baseUrl: String,
    val debug: Boolean,
    val requestTimeout: Long,
    val connectTimeout: Long
)
class PicPayHttpClient(
    private val config: HttpClientConfig,
    private val logWriter: LogWriter
) {
    private val client by lazy {
        HttpClient {
            install(HttpTimeout) {
                requestTimeoutMillis = config.requestTimeout
                connectTimeoutMillis = config.connectTimeout
            }
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
                val safeUrl = config.baseUrl.trim().replace("\"", "")
                url(safeUrl)
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        logWriter.d(TAG.NET_TAG, message)
                    }
                }
                level = if (config.debug) LogLevel.ALL else LogLevel.INFO
            }
        }
    }
    operator fun invoke(): HttpClient = client
}