package com.giovanna.amatucci.desafio_android_picpay.util

import platform.Foundation.NSLog

class IosLogWriter : LogWriter {
    override fun d(tag: String, message: String) {
        NSLog(tag, message)
    }

    override fun w(tag: String, message: String, t: Throwable?) {
        NSLog(tag, message, t)
    }

    override fun e(tag: String, message: String, t: Throwable?) {
        NSLog(tag, message, t)
    }
}