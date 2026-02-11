package com.giovanna.amatucci.desafio_android_picpay.util

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
sealed interface UiText {
    data class DynamicString(val value: String) : UiText
    class StringResource(
        val res: org.jetbrains.compose.resources.StringResource,
        vararg val args: Any
    ) : UiText

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(res, *args)
        }
    }
    suspend fun asStringSuspend(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> getString(res, *args)
        }
    }

}

fun String.format(vararg args: Any?): String {
    var formatted = this
    args.forEach { arg ->
        val regex = Regex("%[sdb]")
        if (regex.containsMatchIn(formatted)) {
            formatted = formatted.replaceFirst(regex, arg.toString())
        }
    }
    return formatted
}