package yandex.school.project.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import yandex.school.project.presentation.components.ErrorItem
import yandex.school.project.presentation.common.Result
import yandex.school.project.presentation.common.CoroutineManager

/**
 * Универсальный экран для отображения состояний загрузки, ошибок и успеха.
 * Автоматически управляет корутинами через CoroutineManager.
 * 
 * @param result Состояние результата (Loading/Error/Success)
 * @param onRetry Функция для повторной попытки (опционально)
 * @param coroutineManager Менеджер корутин для автоматического управления (опционально)
 * @param content Контент для отображения при успешном результате
 */
@Composable
fun <T> ResultScreen(
    result: Result<T>,
    onRetry: (() -> Unit)? = null,
    coroutineManager: CoroutineManager? = null,
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
                onRetryClick = { 
                    if (coroutineManager != null && onRetry != null) {
                        coroutineManager.launchWithCancelPrevious {
                            onRetry()
                        }
                    } else {
                        onRetry?.invoke()
                    }
                }
            )
        }
        is Result.Success -> {
            content(result.data)
        }
    }
} 