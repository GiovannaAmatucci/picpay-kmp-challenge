package com.giovanna.amatucci.desafio_android_picpay.util

import com.giovanna.amatucci.desafio_android_picpay.data.remote.api.PicPayApi
import com.giovanna.amatucci.desafio_android_picpay.data.remote.model.UserResponse
import kotlinx.coroutines.delay

class FakePicPayApi : PicPayApi {
    var resultToReturn: ResultWrapper<List<UserResponse>> = ResultWrapper.NetworkError
    var delayMillis: Long = 0

    override suspend fun getUsers(): ResultWrapper<List<UserResponse>> {
        delay(delayMillis)
        return resultToReturn
    }
}