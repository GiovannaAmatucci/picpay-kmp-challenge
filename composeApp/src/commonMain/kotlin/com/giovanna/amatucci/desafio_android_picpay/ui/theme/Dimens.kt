package com.giovanna.amatucci.desafio_android_picpay.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Dimensions(
    val paddingSmall: Dp = 8.dp,
    val paddingMedium: Dp = 12.dp,
    val paddingLarge: Dp = 16.dp,
    val paddingExtraLarge: Dp = 24.dp,
    val iconAvatarSize: Dp = 52.dp,
    val avatarLoading: Float = 0.5f
)

val LocalDimens = staticCompositionLocalOf { Dimensions() }