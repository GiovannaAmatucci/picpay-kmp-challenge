package com.giovanna.amatucci.desafio_android_picpay.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.giovanna.amatucci.desafio_android_picpay.Res
import com.giovanna.amatucci.desafio_android_picpay.retry_button
import com.giovanna.amatucci.desafio_android_picpay.ui.theme.AppTheme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ActionButton(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    text: StringResource = Res.string.retry_button
) {
    Button(
        onClick = onRetry,
        modifier = modifier.padding(top = AppTheme.dimens.paddingLarge),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(stringResource(text))
    }
}