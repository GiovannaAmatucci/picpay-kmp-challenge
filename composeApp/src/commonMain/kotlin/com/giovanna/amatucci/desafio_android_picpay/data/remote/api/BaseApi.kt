package com.giovanna.amatucci.desafio_android_picpay.data.remote.api

import com.giovanna.amatucci.desafio_android_picpay.util.LogMessages
import com.giovanna.amatucci.desafio_android_picpay.util.LogWriter
import com.giovanna.amatucci.desafio_android_picpay.util.ResultWrapper
import com.giovanna.amatucci.desafio_android_picpay.util.TAG
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.io.IOException
import kotlin.coroutines.cancellation.CancellationException

abstract class BaseApi(
    private val logWriter: LogWriter,
    private val tag: String = TAG.PICPAY_API_TAG
) {
    protected suspend fun <T> safeApiCall(
        call: suspend () -> HttpResponse,
        mapping: suspend (HttpResponse) -> T,
        errorLogMessage: String = LogMessages.API_GET_USERS_ERROR
    ): ResultWrapper<T> {
        return try {
            val response = call()
            if (response.status.isSuccess()) {
                val data = mapping(response)
                logWriter.d(tag, LogMessages.API_DATA_MAPPED)
                ResultWrapper.Success(data)
            } else {
                val statusCode = response.status.value
                val message = "$errorLogMessage (Status: $statusCode)"

                logWriter.e(tag, message)
                ResultWrapper.GenericError(statusCode, message)
            }
        } catch (e: ClientRequestException) {
            val msg = "${LogMessages.API_GET_USERS_CLIENT_EXCEPTION}: ${e.message}"
            logWriter.e(tag, msg)
            ResultWrapper.GenericError(e.response.status.value, e.message)

        } catch (e: RedirectResponseException) {
            val msg = LogMessages.API_GET_USERS_ERROR
            logWriter.e(tag, msg)
            ResultWrapper.GenericError(e.response.status.value, msg)

        } catch (e: ServerResponseException) {
            val msg = LogMessages.API_GET_USERS_ERROR
            logWriter.e(tag, msg)
            ResultWrapper.GenericError(e.response.status.value, msg)

        } catch (e: IOException) {
            logWriter.w(tag, "${LogMessages.API_GET_USERS_NETWORK_ERROR}: ${e.message}")
            ResultWrapper.NetworkError

        } catch (e: Exception) {
            val msg = "${LogMessages.API_GET_USERS_EXCEPTION}: ${e.message}"
            logWriter.e(tag, msg)
            ResultWrapper.GenericError(null, msg)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            val msg = "${LogMessages.API_GET_USERS_EXCEPTION}: ${e.message}"
            logWriter.e(tag, msg)
            ResultWrapper.GenericError(null, msg)
        }
    }
}