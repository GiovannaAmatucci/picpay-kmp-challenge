package com.giovanna.amatucci.desafio_android_picpay.domain.usecase

import com.giovanna.amatucci.desafio_android_picpay.domain.model.UserInfo
import com.giovanna.amatucci.desafio_android_picpay.util.FakeContactsRepository
import com.giovanna.amatucci.desafio_android_picpay.util.ResultWrapper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FetchContactsUseCaseImplTest {
    private lateinit var fakeRepository: FakeContactsRepository
    private lateinit var useCase: FetchContactsUseCaseImpl

    @BeforeTest
    fun setup() {
        fakeRepository = FakeContactsRepository()
        useCase = FetchContactsUseCaseImpl(fakeRepository)
    }

    @Test
    fun invoke_WHEN_called_with_forceRefresh_true_THEN_it_must_pass_parameter_to_the_repository() {
        // GIVEN

        // WHEN
        useCase(forceRefresh = true)

        // THEN
        assertEquals(
            true,
            fakeRepository.capturedForceRefresh,
            "Deveria ter passado 'true' para o repositório"
        )
    }

    @Test
    fun invoke_WHEN_called_with_forceRefresh_false_THEN_it_must_pass_parameter_to_the_repository() {
        // WHEN
        useCase(forceRefresh = false)

        // THEN
        assertEquals(
            false,
            fakeRepository.capturedForceRefresh,
            "Deveria ter passado 'false' para o repositório"
        )
    }

    @Test
    fun invoke_MUST_return_exactly_the_stream_emitted_by_the_repository() = runTest {
        // GIVEN
        val expectedData = listOf(
            UserInfo(1, "Giovanna", "url", "user1"), UserInfo(2, "Amatucci", "url", "user2")
        )
        val expectedResult = ResultWrapper.Success(expectedData)
        fakeRepository.flowToReturn = flowOf(expectedResult)

        // WHEN
        val resultFlow = useCase(false)
        val emittedItem = resultFlow.first()

        // THEN
        assertEquals(
            expectedResult,
            emittedItem,
            "O UseCase deve repassar exatamente o que o Repositório emite"
        )
    }
}