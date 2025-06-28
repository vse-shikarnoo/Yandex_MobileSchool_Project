package yandex.school.project.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

/**
 * Хелпер для управления жизненным циклом ViewModels в вложенной навигации.
 * Решает проблему с незавершенными корутинами при переходах между экранами.
 * 
 * ПРОБЛЕМА:
 * При вложенной навигации ViewModels не уничтожаются при переходе между экранами
 * внутри одного таба, поэтому корутины продолжают работать и могут вызывать:
 * - Утечки памяти
 * - Обновление UI на неактивном экране
 * - Ненужные сетевые запросы
 * 
 * РЕШЕНИЕ:
 * Использование DisposableEffect для отмены корутин при уходе с экрана.
 * 
 * ПРИМЕРЫ ИСПОЛЬЗОВАНИЯ:
 * 
 * 1. Простое использование в экране:
 * ```kotlin
 * @Composable
 * fun MyScreen(viewModel: MyViewModel = hiltViewModel()) {
 *     // Автоматически отменяет корутины при уходе с экрана
 *     rememberViewModelLifecycle(viewModel)
 *     
 *     LaunchedEffect(Unit) {
 *         viewModel.loadData()
 *     }
 * }
 * ```
 * 
 * 2. Использование с временным scope в ViewModel:
 * ```kotlin
 * class MyViewModel : ViewModel() {
 *     fun loadData(): Job {
 *         return ViewModelLifecycleHelper.launchInTemporaryScope(
 *             viewModel = this,
 *             operation = {
 *                 // Операция будет отменена при уходе с экрана
 *                 apiService.getData()
 *             }
 *         )
 *     }
 * }
 * ```
 * 
 * 3. Использование с rememberCoroutineScope:
 * ```kotlin
 * @Composable
 * fun MyScreen(viewModel: MyViewModel = hiltViewModel()) {
 *     val scope = rememberViewModelLifecycleWithScope(viewModel)
 *     
 *     LaunchedEffect(Unit) {
 *         scope.launch {
 *             // Корутины в этом scope будут отменены при уходе с экрана
 *             viewModel.loadData()
 *         }
 *     }
 * }
 * ```
 */
object ViewModelLifecycleHelper {
    
    /**
     * Создает временный scope для операций, который будет отменен при уходе с экрана.
     * Используется для операций, которые должны быть отменены при навигации.
     */
    fun createTemporaryScope(viewModel: ViewModel): CoroutineScope {
        return CoroutineScope(viewModel.viewModelScope.coroutineContext)
    }
    
    /**
     * Запускает операцию в временном scope, который будет отменен при уходе с экрана.
     */
    fun launchInTemporaryScope(
        viewModel: ViewModel,
        operation: suspend () -> Unit
    ): Job {
        return createTemporaryScope(viewModel).launch {
            operation()
        }
    }
    
    /**
     * Запускает сбор Flow в временном scope.
     */
    fun <T> launchFlowCollection(
        viewModel: ViewModel,
        flow: Flow<T>,
        collector: suspend (T) -> Unit
    ): Job {
        return createTemporaryScope(viewModel).launch {
            flow.collectLatest(collector)
        }
    }
}

/**
 * Composable эффект для отмены корутин при уходе с экрана.
 * Используется в экранах с вложенной навигацией для предотвращения утечек памяти.
 * 
 * ПРИМЕР:
 * ```kotlin
 * @Composable
 * fun MyScreen(viewModel: MyViewModel = hiltViewModel()) {
 *     rememberViewModelLifecycle(viewModel)
 *     // ... остальной код экрана
 * }
 * ```
 */
@Composable
fun rememberViewModelLifecycle(viewModel: ViewModel) {
    val scope = remember { ViewModelLifecycleHelper.createTemporaryScope(viewModel) }
    
    DisposableEffect(Unit) {
        onDispose {
            // Отменяем все корутины в scope при уходе с экрана
            scope.cancel()
        }
    }
}

/**
 * Composable эффект для отмены конкретной корутины при уходе с экрана.
 * 
 * ПРИМЕР:
 * ```kotlin
 * @Composable
 * fun MyScreen(viewModel: MyViewModel = hiltViewModel()) {
 *     val job = remember { viewModel.startOperation() }
 *     rememberCancellableJob(viewModel, job)
 * }
 * ```
 */
@Composable
fun rememberCancellableJob(
    viewModel: ViewModel,
    job: Job
) {
    DisposableEffect(Unit) {
        onDispose {
            // Отменяем конкретную корутину при уходе с экрана
            if (job.isActive) {
                job.cancel()
            }
        }
    }
}

/**
 * Composable эффект для управления жизненным циклом ViewModel с использованием rememberCoroutineScope.
 * Более современный подход для управления корутинами в Compose.
 * 
 * ПРИМЕР:
 * ```kotlin
 * @Composable
 * fun MyScreen(viewModel: MyViewModel = hiltViewModel()) {
 *     val scope = rememberViewModelLifecycleWithScope(viewModel)
 *     
 *     LaunchedEffect(Unit) {
 *         scope.launch {
 *             viewModel.loadData()
 *         }
 *     }
 * }
 * ```
 */
@Composable
fun rememberViewModelLifecycleWithScope(viewModel: ViewModel): CoroutineScope {
    val scope = rememberCoroutineScope()
    
    DisposableEffect(Unit) {
        onDispose {
            // Отменяем все корутины в scope при уходе с экрана
            scope.cancel()
        }
    }
    
    return scope
}

/**
 * Composable эффект для запуска операции с автоматической отменой при уходе с экрана.
 * 
 * ПРИМЕР:
 * ```kotlin
 * @Composable
 * fun MyScreen(viewModel: MyViewModel = hiltViewModel()) {
 *     rememberCancellableOperation(viewModel) {
 *         viewModel.loadData()
 *     }
 * }
 * ```
 */
@Composable
fun rememberCancellableOperation(
    viewModel: ViewModel,
    operation: suspend () -> Unit
) {
    val scope = rememberCoroutineScope()
    
    DisposableEffect(Unit) {
        val job = scope.launch {
            operation()
        }
        
        onDispose {
            if (job.isActive) {
                job.cancel()
            }
        }
    }
} 