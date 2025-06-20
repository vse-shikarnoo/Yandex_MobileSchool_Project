package yandex.school.project.ui.common

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import yandex.school.project.utils.NetworkErrorHandler

/**
 * Базовый класс для ViewModel'ей с сетевой логикой
 */
abstract class BaseNetworkViewModel : ViewModel() {
    
    private val TAG = "BaseNetworkViewModel"
    
    /**
     * Выполняет сетевую операцию с повторными попытками
     */
    protected fun <T> executeWithRetry(
        operation: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: (String) -> Unit,
        maxRetries: Int = 3,
        delayMillis: Long = 2000,
        operationName: String = "операция"
    ) {
        viewModelScope.launch {
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
                    
                    // Если критическая ошибка, прекращаем попытки
                    if (NetworkErrorHandler.isCriticalError(e)) {
                        Log.d(TAG, "Критическая ошибка, прекращаем попытки")
                        onError(errorMessage)
                        break
                    }
                    
                    // Если есть еще попытки, ждем и повторяем
                    if (attempt < maxRetries) {
                        Log.d(TAG, "Ожидание ${delayMillis}ms перед следующей попыткой")
                        delay(delayMillis)
                    }
                }
            }
            
            // Если все попытки исчерпаны
            if (!success && lastError != null) {
                Log.e(TAG, "Все попытки исчерпаны. Последняя ошибка: ${NetworkErrorHandler.getErrorType(lastError)} - ${lastError.message}")
                onError(NetworkErrorHandler.handleNetworkError(lastError, operationName))
            }
        }
    }
    
    /**
     * Выполняет сетевую операцию без повторных попыток
     */
    protected fun <T> executeOnce(
        operation: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: (String) -> Unit,
        operationName: String = "операция"
    ) {
        viewModelScope.launch {
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