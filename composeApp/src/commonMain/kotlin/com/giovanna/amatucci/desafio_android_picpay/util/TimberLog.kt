package com.giovanna.amatucci.desafio_android_picpay.util
interface LogWriter {
    fun d(tag: String, message: String)
    fun w(tag: String, message: String, t: Throwable? = null)
    fun e(tag: String, message: String, t: Throwable? = null)
}

