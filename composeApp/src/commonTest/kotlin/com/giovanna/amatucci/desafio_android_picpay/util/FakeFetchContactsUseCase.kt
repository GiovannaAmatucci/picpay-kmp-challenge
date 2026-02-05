package com.giovanna.amatucci.desafio_android_picpay.util

import com.giovanna.amatucci.desafio_android_picpay.domain.model.UserInfo
import com.giovanna.amatucci.desafio_android_picpay.domain.usecase.FetchContactsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeFetchContactsUseCase : FetchContactsUseCase {
    var flowToReturn: Flow<ResultWrapper<List<UserInfo>>> =
        flowOf(ResultWrapper.Success(emptyList()))
    var capturedForceRefresh: Boolean? = null
    var callCount = 0

    override operator fun invoke(forceRefresh: Boolean): Flow<ResultWrapper<List<UserInfo>>> {
        capturedForceRefresh = forceRefresh
        callCount++
        return flowToReturn
    }
}