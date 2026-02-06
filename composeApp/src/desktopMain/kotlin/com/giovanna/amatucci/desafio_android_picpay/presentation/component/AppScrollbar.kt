package com.giovanna.amatucci.desafio_android_picpay.presentation.component

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun AppScrollbar(
    modifier: Modifier, state: LazyListState
) {
    VerticalScrollbar(
        adapter = rememberScrollbarAdapter(state), modifier = modifier
    )
}