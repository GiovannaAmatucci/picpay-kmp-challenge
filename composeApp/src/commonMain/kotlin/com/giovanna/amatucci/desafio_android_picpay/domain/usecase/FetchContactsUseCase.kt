package com.giovanna.amatucci.desafio_android_picpay.domain.usecase

import com.giovanna.amatucci.desafio_android_picpay.domain.model.UserInfo
import com.giovanna.amatucci.desafio_android_picpay.domain.repository.ContactsRepository
import com.giovanna.amatucci.desafio_android_picpay.util.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface FetchContactsUseCase {
    operator fun invoke(forceRefresh: Boolean): Flow<ResultWrapper<List<UserInfo>>>
}

class FetchContactsUseCaseImpl(private val repository: ContactsRepository) : FetchContactsUseCase {
    override fun invoke(forceRefresh: Boolean): Flow<ResultWrapper<List<UserInfo>>> =
        repository.getUsers(forceRefresh = forceRefresh)
}


