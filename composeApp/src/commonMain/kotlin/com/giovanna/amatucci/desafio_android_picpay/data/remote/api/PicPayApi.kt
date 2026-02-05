package com.giovanna.amatucci.desafio_android_picpay.data.remote.api

import com.giovanna.amatucci.desafio_android_picpay.data.remote.model.UserResponse
import com.giovanna.amatucci.desafio_android_picpay.util.LogMessages
import com.giovanna.amatucci.desafio_android_picpay.util.LogWriter
import com.giovanna.amatucci.desafio_android_picpay.util.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
private const val USERS = "users"
interface PicPayApi {
    suspend fun getUsers(): ResultWrapper<List<UserResponse>>
}
class PicPayApiImpl(private val client: HttpClient, logWriter: LogWriter) :
    BaseApi(logWriter), PicPayApi {
    override suspend fun getUsers(): ResultWrapper<List<UserResponse>> {
        return safeApiCall(
            call = { client.get(USERS) },
            mapping = { response -> response.body<List<UserResponse>>() },
            errorLogMessage = LogMessages.API_GET_USERS_ERROR
        )
    }
}