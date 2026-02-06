package com.giovanna.amatucci.desafio_android_picpay.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.giovanna.amatucci.desafio_android_picpay.domain.model.UserInfo
import com.giovanna.amatucci.desafio_android_picpay.getPlatform
import com.giovanna.amatucci.desafio_android_picpay.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshContactList(
    users: List<UserInfo>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    state: LazyListState,
    modifier: Modifier = Modifier
) {
    val usePullGesture = getPlatform().supportsPullToRefresh

    if (usePullGesture) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = modifier,
            state = PullToRefreshState(),
        ) {
            ContactList(users = users, state = state)
        }
    } else {
        Box(modifier = modifier) {
            ContactList(users = users, state = state)
            if (isRefreshing) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center).padding(AppTheme.dimens.paddingMedium)
                )
            }
        }
    }
}
@Composable
private fun ContactList(users: List<UserInfo>, state: LazyListState) {
    LazyColumn(modifier = Modifier.fillMaxSize(), state = state) {
        items(
            items = users, key = { user -> user.id }) { user ->
            ContactItem(user)
        }
    }
}