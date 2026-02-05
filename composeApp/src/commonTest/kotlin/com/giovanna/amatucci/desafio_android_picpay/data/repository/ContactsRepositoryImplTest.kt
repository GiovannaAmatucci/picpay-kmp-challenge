package com.giovanna.amatucci.desafio_android_picpay.data.repository

import com.giovanna.amatucci.desafio_android_picpay.data.local.db.model.ContactUserEntity
import com.giovanna.amatucci.desafio_android_picpay.data.remote.model.UserResponse
import com.giovanna.amatucci.desafio_android_picpay.domain.model.UserInfo
import com.giovanna.amatucci.desafio_android_picpay.util.FakeContactUserDao
import com.giovanna.amatucci.desafio_android_picpay.util.FakeLogWriter
import com.giovanna.amatucci.desafio_android_picpay.util.FakePicPayApi
import com.giovanna.amatucci.desafio_android_picpay.util.ResultWrapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ContactsRepositoryImplTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: ContactsRepositoryImpl
    private lateinit var fakeApi: FakePicPayApi
    private lateinit var fakeDao: FakeContactUserDao
    private val logWriter = FakeLogWriter()

    @BeforeTest
    fun setup() {
        fakeApi = FakePicPayApi()
        fakeDao = FakeContactUserDao()
        repository = ContactsRepositoryImpl(fakeApi, fakeDao, testDispatcher, logWriter)
    }

    @Test
    fun getUsers_WHEN_cache_is_empty_and_API_fails_THEN_should_emit_NetworkError_ONLY() =
        runTest(testDispatcher) {
            // GIVEN
            fakeDao.setInitialData(emptyList())

            fakeApi.apply {
                delayMillis = 10
                resultToReturn = ResultWrapper.NetworkError
            }

            // WHEN
            val results = mutableListOf<ResultWrapper<List<UserInfo>>>()
            backgroundScope.launch(testDispatcher) {
                repository.getUsers(forceRefresh = false).toList(results)
            }
            testScheduler.advanceTimeBy(100)

            // THEN
            assertEquals(1, results.size, "Deve ter apenas 1 emissão")
            assertTrue(results[0] is ResultWrapper.NetworkError, "Emissão deve ser Erro de Rede")
        }

    @Test
    fun getUsers_WHEN_cache_has_data_and_refresh_is_forced_THEN_it_should_emit_cache_AND_error() =
        runTest(testDispatcher) {
            // GIVEN
            val localData = listOf(ContactUserEntity(1, "Gio", "img", "gio"))
            fakeDao.setInitialData(localData)

            fakeApi.apply {
                delayMillis = 10
                resultToReturn = ResultWrapper.NetworkError
            }

            // WHEN
            val results = mutableListOf<ResultWrapper<List<UserInfo>>>()
            backgroundScope.launch(testDispatcher) {
                repository.getUsers(forceRefresh = true).toList(results)
            }

            testScheduler.advanceTimeBy(100)

            // THEN
            assertTrue(results.size >= 2, "Deve ter emitido pelo menos Cache e Erro")

            val firstEmission = results[0]
            assertTrue(firstEmission is ResultWrapper.Success, "Primeira emissão deve ser Cache")
            assertEquals("Gio", (firstEmission).value[0].name)
            val errorEmission = results[1]
            assertTrue(
                errorEmission is ResultWrapper.NetworkError,
                "A segunda emissão deve ser o Erro de Rede (recebido: $errorEmission)"
            )
            if (results.size > 2) {
                assertTrue(
                    results.last() is ResultWrapper.Success,
                    "O repositório volta para o cache após o erro"
                )
            }
        }

    @Test
    fun getUsers_WHEN_API_returns_success_THEN_it_should_save_to_the_database() =
        runTest(testDispatcher) {
            // GIVEN
            val apiResponse = listOf(
                UserResponse(id = 1, name = "Teste", username = "test", img = "url")
            )

            fakeDao.setInitialData(emptyList())

            fakeApi.apply {
                delayMillis = 10
                resultToReturn = ResultWrapper.Success(apiResponse)
            }

            // WHEN
            backgroundScope.launch(testDispatcher) {
                repository.getUsers(forceRefresh = true).toList(mutableListOf())
            }

            testScheduler.advanceTimeBy(100)

            // THEN
            assertEquals(1, fakeDao.updateCallCount, "updateContacts deve ser chamado 1 vez")
        }
}