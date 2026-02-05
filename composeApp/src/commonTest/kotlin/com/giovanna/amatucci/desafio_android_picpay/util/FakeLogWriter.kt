package com.giovanna.amatucci.desafio_android_picpay.util

class FakeLogWriter : LogWriter {
    override fun d(tag: String, message: String) {}
    override fun w(tag: String, message: String, t: Throwable?) {}
    override fun e(tag: String, message: String, t: Throwable?) {}
}