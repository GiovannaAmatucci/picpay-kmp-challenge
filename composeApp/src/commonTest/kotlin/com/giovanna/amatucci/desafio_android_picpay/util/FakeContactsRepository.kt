package com.giovanna.amatucci.desafio_android_picpay.util

import com.giovanna.amatucci.desafio_android_picpay.domain.model.UserInfo
import com.giovanna.amatucci.desafio_android_picpay.domain.repository.ContactsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeContactsRepository : ContactsRepository {
    var capturedForceRefresh: Boolean? = null
    var flowToReturn: Flow<ResultWrapper<List<UserInfo>>> =
        flowOf(ResultWrapper.Success(emptyList()))

    override fun getUsers(forceRefresh: Boolean): Flow<ResultWrapper<List<UserInfo>>> {
        capturedForceRefresh = forceRefresh
        return flowToReturn
    }
}