package com.giovanna.amatucci.desafio_android_picpay.data.repository

import com.giovanna.amatucci.desafio_android_picpay.data.local.db.dao.ContactUserDao
import com.giovanna.amatucci.desafio_android_picpay.data.remote.api.PicPayApi
import com.giovanna.amatucci.desafio_android_picpay.data.remote.mapper.toDomainList
import com.giovanna.amatucci.desafio_android_picpay.data.remote.mapper.toEntityList
import com.giovanna.amatucci.desafio_android_picpay.domain.model.UserInfo
import com.giovanna.amatucci.desafio_android_picpay.domain.repository.ContactsRepository
import com.giovanna.amatucci.desafio_android_picpay.util.ErrorFormat
import com.giovanna.amatucci.desafio_android_picpay.util.LogMessages
import com.giovanna.amatucci.desafio_android_picpay.util.LogWriter
import com.giovanna.amatucci.desafio_android_picpay.util.ResultWrapper
import com.giovanna.amatucci.desafio_android_picpay.util.TAG
import com.giovanna.amatucci.desafio_android_picpay.util.format
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class ContactsRepositoryImpl(
    private val api: PicPayApi,
    private val userDao: ContactUserDao,
    private val ioDispatcher: CoroutineDispatcher,
    private val logWriter: LogWriter
) : ContactsRepository {
    private val tag = TAG.REPO_TAG
    override fun getUsers(forceRefresh: Boolean): Flow<ResultWrapper<List<UserInfo>>> = flow {
        val localData = userDao.getAllUsers().first()
        val hasCachedData = localData.isNotEmpty()
        logWriter.d(tag, LogMessages.REPO_CHECK_CACHE.format(localData.size))

        if (hasCachedData) {
            emit(ResultWrapper.Success(localData.toDomainList()))
        }

        val shouldFetch = forceRefresh || !hasCachedData
        logWriter.d(
            tag, LogMessages.REPO_DECISION.format(forceRefresh, !hasCachedData, shouldFetch)
        )

        if (shouldFetch) {
            logWriter.d(tag, LogMessages.REPO_NETWORK_START)
            try {
                api.getUsers().let { apiResult ->
                    when (apiResult) {
                        is ResultWrapper.Success -> {
                            apiResult.value.let { items ->
                                logWriter.d(
                                    tag, LogMessages.REPO_NETWORK_SUCCESS.format(items.size)
                                )
                                userDao.updateContacts(items.toEntityList())
                            }
                        }

                        is ResultWrapper.GenericError, is ResultWrapper.NetworkError -> {
                            handleApiError(apiResult, hasCachedData)
                            emit(apiResult)
                            if (!hasCachedData) return@flow
                        }
                    }
                }
            } catch (e: Exception) {
                logWriter.e(tag, LogMessages.REPO_ERROR_DATA, e)
                emit(ResultWrapper.GenericError(null, e.message))
                if (!hasCachedData) return@flow
            }
        }
        emitAll(userDao.getAllUsers().map {
            ResultWrapper.Success(it.toDomainList())
        }.distinctUntilChanged())

    }.flowOn(ioDispatcher)

    private fun handleApiError(result: ResultWrapper<List<UserInfo>>, hasCache: Boolean) {
        val errorDetails = when (result) {
            is ResultWrapper.GenericError -> ErrorFormat.ERR_FORMAT_GENERIC.format(
                result.code ?: -1, result.message
            )

            is ResultWrapper.NetworkError -> ErrorFormat.ERR_FORMAT_NETWORK
            else -> ErrorFormat.ERR_FORMAT_UNKNOWN
        }
        if (hasCache) {
            logWriter.w(tag, LogMessages.REPO_NETWORK_FAILURE.format(errorDetails))
        } else {
            logWriter.e(tag, LogMessages.REPO_CRITICAL_ERROR)
        }
    }
}
