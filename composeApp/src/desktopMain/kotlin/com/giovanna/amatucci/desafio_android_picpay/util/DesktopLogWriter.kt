package com.giovanna.amatucci.desafio_android_picpay.util

class DesktopLogWriter : LogWriter {
    override fun d(tag: String, message: String) {
        println("DEBUG [$tag]: $message")
    }

    override fun w(tag: String, message: String, t: Throwable?) {
        println("WARN [$tag]: $message")
        t?.printStackTrace()
    }

    override fun e(tag: String, message: String, t: Throwable?) {
        System.err.println("ERROR [$tag]: $message")
        t?.printStackTrace()
    }
}