package com.giovanna.amatucci.desafio_android_picpay.presentation.feature

import com.giovanna.amatucci.desafio_android_picpay.domain.model.UserInfo
import com.giovanna.amatucci.desafio_android_picpay.util.UiText

data class ContactsUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val users: List<UserInfo> = emptyList(),
    val error: UiText? = null
)

sealed interface UiEffect {
    data class ShowUserMessage(val message: UiText) : UiEffect
}