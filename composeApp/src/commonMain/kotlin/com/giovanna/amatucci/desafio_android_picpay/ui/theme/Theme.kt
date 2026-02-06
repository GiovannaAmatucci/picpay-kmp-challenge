package com.giovanna.amatucci.desafio_android_picpay.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val DarkColorScheme = darkColorScheme(
    primary = PicPayGreenDark,
    onPrimary = White,
    background = DarkBackground,
    onBackground = onBackground,
    surface = DarkBackground,
    onSurface = White,
    onSurfaceVariant = Gray94
)

private val LightColorScheme = lightColorScheme(
    primary = PicPayGreen,
    onPrimary = White,
    background = White,
    onBackground = DarkBackground
)

object AppTheme {
    val dimens: Dimensions
        @Composable get() = LocalDimens.current
}

@Composable
fun PicpayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val dimensions = Dimensions()
    CompositionLocalProvider(LocalDimens provides dimensions) {
        MaterialTheme(
            colorScheme = colorScheme, typography = Typography, content = content
        )
    }
}