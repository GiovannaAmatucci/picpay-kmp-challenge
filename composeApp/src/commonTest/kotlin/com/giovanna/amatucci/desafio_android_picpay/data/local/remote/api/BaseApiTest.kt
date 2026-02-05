package com.giovanna.amatucci.desafio_android_picpay.data.local.remote.api

import com.giovanna.amatucci.desafio_android_picpay.data.remote.api.BaseApi
import com.giovanna.amatucci.desafio_android_picpay.util.FakeLogWriter
import com.giovanna.amatucci.desafio_android_picpay.util.LogWriter
import com.giovanna.amatucci.desafio_android_picpay.util.ResultWrapper
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import kotlin.test.Test
import kotlin.test.assertTrue

class BaseApiTest {
    class TestApi(logWriter: LogWriter) : BaseApi(logWriter) {
        suspend fun executeCall(shouldThrow: Exception? = null): ResultWrapper<String> {
            return safeApiCall(call = {
                if (shouldThrow != null) throw shouldThrow
                error("Not needed for this specific test logic")
            }, mapping = { "Success" })
        }
    }

    @Test
    fun safeApiCall_WHEN_NetworkError_THEN_returns_NetworkError() = runTest {
        // GIVEN
        val api = TestApi(FakeLogWriter())
        val exception = IOException("Network failure simulation")

        // WHEN
        val result = api.executeCall(shouldThrow = exception)
        println("Resultado retornado: $result")

        // THEN
        assertTrue(result is ResultWrapper.NetworkError, "Esperado NetworkError, mas recebeu $result")
    }
}