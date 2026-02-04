package com.giovanna.amatucci.desafio_android_picpay.presentation.feature

sealed interface ContactsUiEvent {
    data object OnRefresh : ContactsUiEvent
    data object OnRetry : ContactsUiEvent
    data object OnErrorConsumed : ContactsUiEvent
}