package com.giovanna.amatucci.desafio_android_picpay.presentation.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giovanna.amatucci.desafio_android_picpay.Res
import com.giovanna.amatucci.desafio_android_picpay.domain.model.UserInfo
import com.giovanna.amatucci.desafio_android_picpay.domain.usecase.FetchContactsUseCase
import com.giovanna.amatucci.desafio_android_picpay.error_default_message
import com.giovanna.amatucci.desafio_android_picpay.error_network_message
import com.giovanna.amatucci.desafio_android_picpay.util.ConnectivityObserver
import com.giovanna.amatucci.desafio_android_picpay.util.LogMessages
import com.giovanna.amatucci.desafio_android_picpay.util.LogWriter
import com.giovanna.amatucci.desafio_android_picpay.util.ResultWrapper
import com.giovanna.amatucci.desafio_android_picpay.util.TAG
import com.giovanna.amatucci.desafio_android_picpay.util.UiText
import com.giovanna.amatucci.desafio_android_picpay.util.format
import com.giovanna.amatucci.desafio_android_picpay.util.toUiText
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContactsViewModel(
    private val fetchContactsUseCase: FetchContactsUseCase,
    private val connectivityObserver: ConnectivityObserver,
    private val logWriter: LogWriter
) : ViewModel() {
    private val tag = TAG.CONTACTS_VM_TAG
    private val _uiState = MutableStateFlow(ContactsUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()
    private val _refreshTrigger = MutableSharedFlow<Boolean>(replay = TRIGGER_REPLAY)
    private val _uiEffect = MutableSharedFlow<UiEffect>(
        replay = EFFECT_REPLAY,
        extraBufferCapacity = EXTRA_BUFFER_CAPACITY,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        logWriter.d(tag, LogMessages.VM_INIT)
        setupDataPipeline()
        setupConnectivityObserver()
        triggerRefresh(force = false)
    }
    fun onEvent(event: ContactsUiEvent) {
        when (event) {
            ContactsUiEvent.OnRefresh -> triggerRefresh(force = true)
            ContactsUiEvent.OnRetry -> triggerRefresh(force = true)
            ContactsUiEvent.OnErrorConsumed -> {
                _uiState.update { it.copy(error = null) }
            }
        }
    }
    private fun triggerRefresh(force: Boolean) {
        viewModelScope.launch {
            _refreshTrigger.emit(force)
        }
    }
    private fun setupDataPipeline() {
        viewModelScope.launch {
            _refreshTrigger.collectLatest { forceRefresh ->
                fetchContacts(forceRefresh)
            }
        }
    }
    private suspend fun fetchContacts(forceRefresh: Boolean) {
        fetchContactsUseCase(forceRefresh).onStart {
            logWriter.d(tag, LogMessages.VM_PROCESS_START.format(forceRefresh))
            _uiState.update { current ->
                current.copy(
                    isRefreshing = forceRefresh, isLoading = current.users.isEmpty(), error = null
                )
            }
        }.catch { e ->
            logWriter.e(tag, LogMessages.VM_STATE_ERROR, e)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isRefreshing = false,
                    error = UiText.StringResource(Res.string.error_default_message)
                )
            }
        }.collect { result ->
            handleResult(result)
        }
    }
    private fun handleResult(result: ResultWrapper<List<UserInfo>>) {
        _uiState.update { currentState ->
            when (result) {
                is ResultWrapper.Success -> {
                    logWriter.d(tag, LogMessages.VM_STATE_SUCCESS.format(result.value.size))
                    currentState.copy(
                        isLoading = false, isRefreshing = false, users = result.value, error = null
                    )
                }

                is ResultWrapper.NetworkError -> {
                    logWriter.w(tag, LogMessages.VM_STATE_NETWORK_FAILURE)
                    handleError(currentState, UiText.StringResource(Res.string.error_network_message))
                }

                is ResultWrapper.GenericError -> {
                    logWriter.e(tag, LogMessages.VM_STATE_ERROR.format(result.message))
                    handleError(currentState, result.toUiText())
                }
            }
        }
    }
    private fun handleError(currentState: ContactsUiState, errorUiText: UiText): ContactsUiState {
        currentState.users.isNotEmpty().let { hasContent ->
            return if (hasContent) {
                sendEffect(UiEffect.ShowUserMessage(errorUiText))
                currentState.copy(isLoading = false, isRefreshing = false)
            } else {
                currentState.copy(
                    isLoading = false, isRefreshing = false, error = errorUiText
                )
            }
        }
    }
    private fun setupConnectivityObserver() {
        connectivityObserver.observe()
            .distinctUntilChanged()
            .filter { it == ConnectivityObserver.Status.Available }
            .onEach {
                _uiState.value.let { current ->
                    val needsData =
                        current.error != null || (current.users.isEmpty() && !current.isLoading)
                    if (needsData) {
                        logWriter.d(tag, LogMessages.VM_STATE_LOADING_NETWORK)
                        triggerRefresh(force = true)
                    }
                }
            }.launchIn(viewModelScope)
    }
    private fun sendEffect(effect: UiEffect) {
        viewModelScope.launch {
            _uiEffect.emit(effect)
        }
    }
}

private const val TRIGGER_REPLAY = 1
private const val EFFECT_REPLAY = 0
private const val EXTRA_BUFFER_CAPACITY = 1

