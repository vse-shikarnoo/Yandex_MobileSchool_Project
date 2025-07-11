package yandex.school.project.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

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
    modifier: Modifier = Modifier,
    result: yandex.school.project.core.utils.Result<T>,
    onRetry: (() -> Unit)? = null,
    coroutineManager: yandex.school.project.core.utils.CoroutineManager? = null,
    content: @Composable (T) -> Unit
) {
    when (result) {
        is yandex.school.project.core.utils.Result.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is yandex.school.project.core.utils.Result.Error -> {
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

        is yandex.school.project.core.utils.Result.Success -> {
            content(result.data)
        }
    }
} 