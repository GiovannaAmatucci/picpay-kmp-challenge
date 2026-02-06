package com.giovanna.amatucci.desafio_android_picpay.presentation.component

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
 expect fun AppScrollbar(
    modifier: Modifier = Modifier, state: LazyListState
)