package com.giovanna.amatucci.desafio_android_picpay.presentation.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.giovanna.amatucci.desafio_android_picpay.Res
import com.giovanna.amatucci.desafio_android_picpay.domain.model.UserInfo
import com.giovanna.amatucci.desafio_android_picpay.title
import com.giovanna.amatucci.desafio_android_picpay.ui.theme.AppTheme
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshContactList(
    users: List<UserInfo>, isRefreshing: Boolean, onRefresh: () -> Unit
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing, onRefresh = onRefresh, modifier = Modifier.fillMaxSize()
    ) {
        ContactList(users = users)
    }
}

@Composable
private fun ContactList(users: List<UserInfo>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text(
                text = stringResource(Res.string.title),
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(
                    start = AppTheme.dimens.paddingLarge,
                    top = AppTheme.dimens.paddingExtraLarge,
                    bottom = AppTheme.dimens.paddingLarge
                )
            )
        }
        items(
            items = users, key = { user -> user.id }) { user ->
            ContactItem(user)
        }
    }
}