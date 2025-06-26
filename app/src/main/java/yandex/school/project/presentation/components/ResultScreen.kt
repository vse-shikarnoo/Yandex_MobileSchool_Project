package yandex.school.project.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import yandex.school.project.presentation.components.ErrorItem
import yandex.school.project.presentation.common.Result

@Composable
fun <T> ResultScreen(
    result: Result<T>,
    onRetry: (() -> Unit)? = null,
    content: @Composable (T) -> Unit
) {
    when (result) {
        is Result.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is Result.Error -> {
            ErrorItem(
                errorMessage = result.message,
                onRetryClick = { onRetry?.invoke() }
            )
        }
        is Result.Success -> {
            content(result.data)
        }
    }
} 