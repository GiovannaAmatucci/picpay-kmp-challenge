package com.giovanna.amatucci.desafio_android_picpay.domain.repository

import com.giovanna.amatucci.desafio_android_picpay.domain.model.UserInfo
import com.giovanna.amatucci.desafio_android_picpay.util.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
   fun getUsers(forceRefresh: Boolean): Flow<ResultWrapper<List<UserInfo>>>
}