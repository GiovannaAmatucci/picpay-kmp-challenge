package com.giovanna.amatucci.desafio_android_picpay.util

import platform.Foundation.NSLog

class IosLogWriter : LogWriter {
    override fun d(tag: String, message: String) {
        NSLog("%s: %s", tag, message)
    }

    override fun w(tag: String, message: String, t: Throwable?) {
        val errorMessage = t?.toString() ?: ""
        NSLog("%s: %s Warning: %s", tag, message, errorMessage)
    }

    override fun e(tag: String, message: String, t: Throwable?) {
        val stackTrace = t?.stackTraceToString() ?: ""
        NSLog("%s: %s Error: %s", tag, message, stackTrace)
    }
}