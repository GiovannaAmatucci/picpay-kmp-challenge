package com.giovanna.amatucci.desafio_android_picpay.util

import timber.log.Timber

class AndroidLogWriter : LogWriter {
    override fun d(tag: String, message: String) {
        Timber.tag(tag).d(message)
    }

    override fun w(tag: String, message: String, t: Throwable?) {
        Timber.tag(tag).w(t, message)
    }

    override fun e(tag: String, message: String, t: Throwable?) {
        Timber.tag(tag).e(t, message)
    }
}