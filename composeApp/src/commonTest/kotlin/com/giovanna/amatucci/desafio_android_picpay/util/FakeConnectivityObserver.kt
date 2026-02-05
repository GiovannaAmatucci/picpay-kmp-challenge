package com.giovanna.amatucci.desafio_android_picpay.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeConnectivityObserver : ConnectivityObserver {
    val status = MutableStateFlow(ConnectivityObserver.Status.Unavailable)

    override fun observe(): Flow<ConnectivityObserver.Status> = status
}