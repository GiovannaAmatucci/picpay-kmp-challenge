package com.giovanna.amatucci.desafio_android_picpay.util

import com.giovanna.amatucci.desafio_android_picpay.Res
import com.giovanna.amatucci.desafio_android_picpay.error_default_message

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val message: String? = null) :
        ResultWrapper<Nothing>()

    object NetworkError : ResultWrapper<Nothing>()
}

fun ResultWrapper.GenericError.toUiText(): UiText {
    return if (this.code in 500..599) {
        UiText.StringResource(Res.string.error_default_message)
    } else {
        UiText.DynamicString(this.message ?: "")
    }
}