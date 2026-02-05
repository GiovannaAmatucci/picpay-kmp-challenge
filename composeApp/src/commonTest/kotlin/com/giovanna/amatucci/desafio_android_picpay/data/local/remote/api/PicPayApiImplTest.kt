package com.giovanna.amatucci.desafio_android_picpay.data.remote.api

import com.giovanna.amatucci.desafio_android_picpay.util.FakeLogWriter
import com.giovanna.amatucci.desafio_android_picpay.util.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PicPayApiImplTest {
    private fun createMockClient(engine: MockEngine): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }

    @Test
    fun getUsers_WHEN_API_returns_200_OK_THEN_return_Success_with_list() = runTest {
        // GIVEN
        val jsonResponse = """
            [
                {"id": 1, "name": "Giovanna", "img": "url1", "username": "gio"},
                {"id": 2, "name": "PicPay", "img": "url2", "username": "picpay"}
            ]
        """.trimIndent()

        val mockEngine = MockEngine { _ ->
            respond(
                content = jsonResponse,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val client = createMockClient(mockEngine)
        val api = PicPayApiImpl(client, FakeLogWriter())

        // WHEN
        val result = api.getUsers()

        // THEN
        assertTrue(result is ResultWrapper.Success)
        val data = (result).value
        assertEquals(2, data.size)
        assertEquals("Giovanna", data[0].name)
    }

    @Test
    fun getUsers_WHEN_API_returns_500_Server_Error_THEN_returns_GenericError() = runTest {
        // GIVEN
        val mockEngine = MockEngine { _ ->
            respondError(HttpStatusCode.InternalServerError)
        }

        val client = createMockClient(mockEngine)
        val api = PicPayApiImpl(client, FakeLogWriter())

        // WHEN
        val result = api.getUsers()

        // THEN
        assertTrue(result is ResultWrapper.GenericError)
        assertEquals(500, (result).code)
    }

    @Test
    fun getUsers_WHEN_a_connection_error_occurs_THEN_return_NetworkError() = runTest {
        // GIVEN
        val mockEngine = MockEngine { _ ->
            throw IOException("Connection refused simulation")
        }

        val client = createMockClient(mockEngine)
        val api = PicPayApiImpl(client, FakeLogWriter())

        // WHEN
        val result = api.getUsers()

        // THEN
        assertTrue(result is ResultWrapper.NetworkError)
    }
}