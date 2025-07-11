package yandex.school.project.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Менеджер корутин для ViewModels с автоматической отменой при уходе с экрана.
 * Решает проблему с незавершенными корутинами в вложенной навигации.
 */
class CoroutineManager(private val viewModel: ViewModel) {
    
    private val scope = CoroutineScope(viewModel.viewModelScope.coroutineContext)
    private var currentJob: Job? = null
    
    /**
     * Запускает операцию с автоматической отменой предыдущей.
     * @param operation Операция для выполнения
     * @return Job для контроля корутины
     */
    fun launchWithCancelPrevious(operation: suspend () -> Unit): Job {
        currentJob?.cancel()
        currentJob = scope.launch {
            operation()
        }
        return currentJob!!
    }
    
    /**
     * Отменяет текущую корутину.
     */
    fun cancelCurrent() {
        currentJob?.cancel()
        currentJob = null
    }
    
    /**
     * Отменяет все корутины в scope.
     */
    fun cancelAll() {
        scope.cancel()
    }
    
    /**
     * Проверяет, активна ли текущая корутина.
     */
    fun isActive(): Boolean = currentJob?.isActive == true
    
    /**
     * Возвращает текущую корутину (может быть null).
     */
    fun getCurrentJob(): Job? = currentJob
}

/**
 * Composable эффект для автоматического управления корутинами ViewModel.
 * Создает CoroutineManager и автоматически отменяет корутины при уходе с экрана.
 * 
 * @param viewModel ViewModel для управления корутинами
 * @return CoroutineManager для запуска операций
 */
@Composable
fun rememberCoroutineManager(viewModel: ViewModel): CoroutineManager {
    val manager = remember { CoroutineManager(viewModel) }
    
    DisposableEffect(Unit) {
        onDispose {
            // Отменяем все корутины при уходе с экрана
            manager.cancelAll()
        }
    }
    
    return manager
}