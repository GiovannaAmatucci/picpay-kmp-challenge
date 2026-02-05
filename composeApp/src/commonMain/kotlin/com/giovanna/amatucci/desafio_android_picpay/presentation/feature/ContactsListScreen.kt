package com.giovanna.amatucci.desafio_android_picpay.presentation.feature

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.giovanna.amatucci.desafio_android_picpay.Res
import com.giovanna.amatucci.desafio_android_picpay.empty_state_message
import com.giovanna.amatucci.desafio_android_picpay.presentation.component.ActionButton
import com.giovanna.amatucci.desafio_android_picpay.presentation.component.ErrorComponent
import com.giovanna.amatucci.desafio_android_picpay.presentation.component.PullToRefreshContactList
import com.giovanna.amatucci.desafio_android_picpay.ui.theme.AppTheme
import com.giovanna.amatucci.desafio_android_picpay.util.UiText
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
@Composable
fun ContactsListScreen(
    viewModel: ContactsViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current
    val effect = viewModel.uiEffect

    LaunchedEffect(effect, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            effect.collectLatest { effect ->
                when (effect) {
                    is UiEffect.ShowUserMessage -> {
                        val messageText = effect.message.asStringSuspend()

                        snackbarHostState.showSnackbar(
                            message = messageText,
                            withDismissAction = true,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }
    ContactsListContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onEvent = { viewModel.onEvent(it) }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ContactsListContent(
    state: ContactsUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (ContactsUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    state.apply {
        Scaffold(
            modifier = modifier, snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { contentPadding ->
            Surface(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                if (users.isNotEmpty()) {
                    Box(modifier = Modifier.testTag(TestTags.CONTENT_LIST)) {
                        PullToRefreshContactList(
                            users = users,
                            isRefreshing = isRefreshing,
                            onRefresh = { onEvent(ContactsUiEvent.OnRefresh) })
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        when {
                            isLoading -> {
                                LoadingState(Modifier.testTag(TestTags.LOADING))
                            }

                            error != null -> {
                                ErrorState(
                                    message = error,
                                    onRetry = { onEvent(ContactsUiEvent.OnRetry) },
                                    modifier = Modifier.testTag(TestTags.ERROR_SCREEN),
                                )
                            }

                            else -> {
                                EmptyState(
                                    onRetry = { onEvent(ContactsUiEvent.OnRetry) },
                                    modifier = Modifier.testTag(TestTags.EMPTY_STATE)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
@Composable
private fun EmptyState(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.empty_state_message),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        ActionButton(
            onRetry = onRetry, modifier = Modifier.padding(top = AppTheme.dimens.paddingLarge)
        )
    }
}
@Composable
private fun ErrorState(message: UiText, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ErrorComponent(message = message)
        ActionButton(
            onRetry = onRetry, modifier = Modifier.padding(top = AppTheme.dimens.paddingLarge)
        )
    }
}
object TestTags {
    const val LOADING = "LoadingState"
    const val ERROR_SCREEN = "ErrorScreen"
    const val CONTENT_LIST = "ContentList"
    const val EMPTY_STATE = "EmptyState"
}



