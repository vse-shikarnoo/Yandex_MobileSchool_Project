package yandex.school.project.core.utils

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Хелпер для выполнения сетевых операций с поддержкой повторов и обработки ошибок.
 * Единственная ответственность: выполнение сетевых операций с автоматическими повторами и обработкой ошибок.
 */
class NetworkOperationHelper {
    private val TAG = "NetworkOperationHelper"
    private val DEFAULT_OPERATION_NAME = "операция"

    fun <T> executeWithRetry(
        scope: CoroutineScope,
        operation: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: (String) -> Unit,
        maxRetries: Int = 3,
        delayMillis: Long = 2000,
        operationName: String = DEFAULT_OPERATION_NAME
    ) {
        scope.launch {
            var attempt = 0
            var success = false
            var lastError: Exception? = null

            while (attempt < maxRetries && !success) {
                try {
                    Log.d(TAG, "Попытка выполнения $operationName: ${attempt + 1}")
                    val result = operation()
                    Log.d(TAG, "$operationName успешно выполнена")
                    onSuccess(result)
                    success = true
                } catch (e: Exception) {
                    lastError = e
                    attempt++

                    val errorMessage = NetworkErrorHandler.handleNetworkError(e, operationName)
                    Log.e(TAG, "Ошибка при выполнении $operationName (попытка $attempt): ${NetworkErrorHandler.getErrorType(e)}")

                    if (NetworkErrorHandler.isCriticalError(e)) {
                        Log.d(TAG, "Критическая ошибка, прекращаем попытки")
                        onError(errorMessage)
                        break
                    }
                    if (attempt < maxRetries) {
                        Log.d(TAG, "Ожидание ${delayMillis}ms перед следующей попыткой")
                        delay(delayMillis)
                    }
                }
            }
            if (!success && lastError != null) {
                Log.e(TAG, "Все попытки исчерпаны. Последняя ошибка: ${NetworkErrorHandler.getErrorType(lastError)} - ${lastError.message}")
                onError(NetworkErrorHandler.handleNetworkError(lastError, operationName))
            }
        }
    }

    fun <T> executeOnce(
        scope: CoroutineScope,
        operation: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: (String) -> Unit,
        operationName: String = DEFAULT_OPERATION_NAME
    ) {
        scope.launch {
            try {
                Log.d(TAG, "Выполнение $operationName")
                val result = operation()
                Log.d(TAG, "$operationName успешно выполнена")
                onSuccess(result)
            } catch (e: Exception) {
                val errorMessage = NetworkErrorHandler.handleNetworkError(e, operationName)
                Log.e(TAG, "Ошибка при выполнении $operationName: ${NetworkErrorHandler.getErrorType(e)}")
                onError(errorMessage)
            }
        }
    }
} 