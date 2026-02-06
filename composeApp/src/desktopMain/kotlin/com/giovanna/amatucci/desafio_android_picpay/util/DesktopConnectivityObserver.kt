package com.giovanna.amatucci.desafio_android_picpay.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class DesktopConnectivityObserver : ConnectivityObserver {

    override fun observe(): Flow<ConnectivityObserver.Status> = flow {
        while (true) {
            val status = if (checkConnection()) {
                ConnectivityObserver.Status.Available
            } else {
                ConnectivityObserver.Status.Lost
            }

            emit(status)
            delay(3000L)
        }
    }.distinctUntilChanged()

    private fun checkConnection(): Boolean {
        return try {
            val timeoutMs = 1500
            val socket = Socket()
            val socketAddress = InetSocketAddress("8.8.8.8", 53)

            socket.connect(socketAddress, timeoutMs)
            socket.close()
            true
        } catch (e: IOException) {
            false
        }
    }
}