package com.giovanna.amatucci.desafio_android_picpay.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
class DesktopConnectivityObserver : ConnectivityObserver {

    override fun observe(): Flow<ConnectivityObserver.Status> = flow {
        while (true) {
            val status = checkConnection()
            val result =
                if (status) ConnectivityObserver.Status.Available else ConnectivityObserver.Status.Lost
            emit(result)
            delay(3000L)
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun checkConnection(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                Socket().use { socket ->
                    val socketAddress = InetSocketAddress("8.8.8.8", 53)
                    socket.connect(socketAddress, 1500)
                    true
                }
            } catch (e: IOException) {
                false
            }
        }
    }
}