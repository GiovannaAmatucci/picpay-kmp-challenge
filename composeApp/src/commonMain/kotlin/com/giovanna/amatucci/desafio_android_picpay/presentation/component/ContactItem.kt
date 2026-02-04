package com.giovanna.amatucci.desafio_android_picpay.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.giovanna.amatucci.desafio_android_picpay.Res
import com.giovanna.amatucci.desafio_android_picpay.contact_avatar_description
import com.giovanna.amatucci.desafio_android_picpay.domain.model.UserInfo
import com.giovanna.amatucci.desafio_android_picpay.ic_launcher_round
import com.giovanna.amatucci.desafio_android_picpay.ui.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ContactItem(
    userInfo: UserInfo,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppTheme.dimens.paddingExtraLarge,
                vertical = AppTheme.dimens.paddingMedium
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        userInfo.apply {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current).data(img).crossfade(true)
                    .build(),
                contentDescription = stringResource(resource = Res.string.contact_avatar_description, name),
                placeholder = painterResource(Res.drawable.ic_launcher_round),
                error = painterResource( Res.drawable.ic_launcher_round),
                modifier = Modifier
                    .size(AppTheme.dimens.iconAvatarSize)
                    .clip(CircleShape),
            )
            Column(modifier = Modifier.padding(start = AppTheme.dimens.paddingLarge)) {
                Text(
                    text = "@${userInfo.username}",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = name,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}