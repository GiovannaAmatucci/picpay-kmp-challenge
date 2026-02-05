package com.giovanna.amatucci.desafio_android_picpay.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.giovanna.amatucci.desafio_android_picpay.Res
import com.giovanna.amatucci.desafio_android_picpay.error_title
import com.giovanna.amatucci.desafio_android_picpay.ui.theme.AppTheme
import com.giovanna.amatucci.desafio_android_picpay.util.UiText
import org.jetbrains.compose.resources.stringResource
@Composable
fun ErrorComponent(
    message: UiText, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(AppTheme.dimens.paddingExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.error_title),
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = message.asString(),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = AppTheme.dimens.paddingSmall)
        )
    }
}