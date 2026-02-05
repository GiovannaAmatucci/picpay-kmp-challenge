package com.giovanna.amatucci.desafio_android_picpay.presentation.feature

import com.giovanna.amatucci.desafio_android_picpay.Res
import com.giovanna.amatucci.desafio_android_picpay.domain.model.UserInfo
import com.giovanna.amatucci.desafio_android_picpay.error_default_message
import com.giovanna.amatucci.desafio_android_picpay.error_network_message
import com.giovanna.amatucci.desafio_android_picpay.util.ConnectivityObserver
import com.giovanna.amatucci.desafio_android_picpay.util.FakeConnectivityObserver
import com.giovanna.amatucci.desafio_android_picpay.util.FakeFetchContactsUseCase
import com.giovanna.amatucci.desafio_android_picpay.util.FakeLogWriter
import com.giovanna.amatucci.desafio_android_picpay.util.ResultWrapper
import com.giovanna.amatucci.desafio_android_picpay.util.UiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ContactsViewModelTest {
    private lateinit var viewModel: ContactsViewModel
    private lateinit var fakeUseCase: FakeFetchContactsUseCase
    private lateinit var fakeConnectivity: FakeConnectivityObserver
    private lateinit var fakeLogWriter: FakeLogWriter
    private val mockUser = UserInfo(id = 1, name = "Test", img = "url", username = "test")

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        fakeUseCase = FakeFetchContactsUseCase()
        fakeConnectivity = FakeConnectivityObserver()
        fakeLogWriter = FakeLogWriter()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() {
        viewModel = ContactsViewModel(fakeUseCase, fakeConnectivity, fakeLogWriter)
    }

    @Test
    fun init_SHOULD_load_contacts_successfully_WHEN_usecase_returns_success() = runTest {
        // GIVEN
        val users = listOf(mockUser)
        fakeUseCase.flowToReturn = flowOf(ResultWrapper.Success(users))
        fakeConnectivity.status.value = ConnectivityObserver.Status.Unavailable

        // WHEN
        createViewModel()

        // THEN
        val state = viewModel.uiState.value
        assertFalse(state.isLoading, "Loading deve ser falso após sucesso")
        assertNull(state.error, "Erro deve ser nulo")
        assertEquals(users, state.users, "Lista de usuários deve corresponder")
    }

    @Test
    fun init_SHOULD_show_network_error_WHEN_usecase_returns_NetworkError() = runTest {
        // GIVEN
        fakeUseCase.flowToReturn = flowOf(ResultWrapper.NetworkError)
        fakeConnectivity.status.value = ConnectivityObserver.Status.Unavailable

        // WHEN
        createViewModel()

        // THEN
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.error is UiText.StringResource, "Erro deve ser do tipo StringResource")

        val errorObj = state.error
        assertEquals(Res.string.error_network_message, errorObj.res)
    }

    @Test
    fun init_SHOULD_show_generic_error_WHEN_usecase_returns_GenericError() = runTest {
        // GIVEN
        val genericError = ResultWrapper.GenericError(500, "Server Error")
        fakeUseCase.flowToReturn = flowOf(genericError)
        fakeConnectivity.status.value = ConnectivityObserver.Status.Unavailable

        // WHEN
        createViewModel()

        // THEN
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.error)
    }

    @Test
    fun fetchContacts_SHOULD_catch_exception_AND_show_default_error_WHEN_usecase_throws() =
        runTest {
            // GIVEN
            fakeUseCase.flowToReturn = flow { throw RuntimeException("Crash!") }
            fakeConnectivity.status.value = ConnectivityObserver.Status.Unavailable

            // WHEN
            createViewModel()

            // THEN
            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertFalse(state.isRefreshing)

            assertNotNull(state.error)
            val errorObj = state.error as UiText.StringResource
            assertEquals(Res.string.error_default_message, errorObj.res)
        }

    @Test
    fun onEvent_OnRefresh_SHOULD_force_refresh_AND_set_isRefreshing_true() = runTest {
        // GIVEN
        fakeUseCase.flowToReturn = flowOf(ResultWrapper.Success(listOf(mockUser)))
        fakeConnectivity.status.value = ConnectivityObserver.Status.Unavailable
        createViewModel()

        val newUsers = listOf(mockUser, mockUser)
        fakeUseCase.flowToReturn = flow {
            emit(ResultWrapper.Success(newUsers))
        }

        fakeUseCase.capturedForceRefresh = null

        // WHEN
        viewModel.onEvent(ContactsUiEvent.OnRefresh)
        testScheduler.advanceUntilIdle()

        // THEN
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(newUsers, state.users)
        assertEquals(
            true, fakeUseCase.capturedForceRefresh, "Deve chamar usecase com forceRefresh=true"
        )
    }

    @Test
    fun onEvent_OnErrorConsumed_SHOULD_clear_error() = runTest {
        // GIVEN
        fakeUseCase.flowToReturn = flowOf(ResultWrapper.NetworkError)
        fakeConnectivity.status.value = ConnectivityObserver.Status.Unavailable
        createViewModel()

        assertNotNull(viewModel.uiState.value.error, "Pré-condição: deve ter erro")

        // WHEN
        viewModel.onEvent(ContactsUiEvent.OnErrorConsumed)

        // THEN
        assertNull(viewModel.uiState.value.error, "Erro deve ser limpo")
    }

    @Test
    fun connectivity_SHOULD_trigger_refresh_WHEN_internet_returns_AND_has_error() = runTest {
        // GIVEN
        fakeConnectivity.status.value = ConnectivityObserver.Status.Unavailable
        fakeUseCase.flowToReturn = flowOf(ResultWrapper.NetworkError)
        createViewModel()

        assertNotNull(viewModel.uiState.value.error)
        val recoveredUsers = listOf(mockUser)
        fakeUseCase.flowToReturn = flowOf(ResultWrapper.Success(recoveredUsers))

        fakeUseCase.capturedForceRefresh = null

        // WHEN
        fakeConnectivity.status.value = ConnectivityObserver.Status.Available
        testScheduler.advanceUntilIdle()

        // THEN
        assertEquals(true, fakeUseCase.capturedForceRefresh, "Internet voltou, deve forçar refresh")

        val state = viewModel.uiState.value
        assertEquals(recoveredUsers, state.users)
        assertNull(state.error)
    }

    @Test
    fun connectivity_SHOULD_NOT_trigger_refresh_WHEN_internet_returns_BUT_data_is_valid() =
        runTest {
            // GIVEN
            fakeConnectivity.status.value = ConnectivityObserver.Status.Unavailable
            fakeUseCase.flowToReturn = flowOf(ResultWrapper.Success(listOf(mockUser)))
            createViewModel()
            assertNull(viewModel.uiState.value.error)
            assertTrue(viewModel.uiState.value.users.isNotEmpty())

            fakeUseCase.callCount = 0

            // WHEN
            fakeConnectivity.status.value = ConnectivityObserver.Status.Available
            testScheduler.advanceUntilIdle()

            // THEN
            assertEquals(
                0, fakeUseCase.callCount, "Não deve chamar refresh se já tem dados válidos"
            )
        }
}