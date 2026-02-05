package com.giovanna.amatucci.desafio_android_picpay.util

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<Status>
    enum class Status { Available, Unavailable, Lost, Losing
    }
}

