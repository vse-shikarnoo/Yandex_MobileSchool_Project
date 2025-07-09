package yandex.school.project.presentation.screens.category

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import yandex.school.project.core.domain.entities.Category
import yandex.school.project.core.domain.usecases.category.GetCategoriesUseCase
import yandex.school.project.core.utils.Result
import javax.inject.Inject
import yandex.school.project.core.utils.NetworkOperationHelper
import androidx.lifecycle.viewModelScope

/**
 * ViewModel для экрана категорий, управляющий состоянием и загрузкой списка категорий.
 * Единственная ответственность: управление состоянием UI и загрузка списка всех категорий.
 */
class CategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: yandex.school.project.core.domain.usecases.category.GetCategoriesUseCase,
    private val networkHelper: yandex.school.project.core.utils.NetworkOperationHelper
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<yandex.school.project.core.utils.Result<List<yandex.school.project.core.domain.entities.Category>>>(
        yandex.school.project.core.utils.Result.Loading)
    val uiState: StateFlow<yandex.school.project.core.utils.Result<List<yandex.school.project.core.domain.entities.Category>>> = _uiState.asStateFlow()

    init {
        loadCategoriesWithRetry()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("${this::class.java}", "onCleared: ")
    }

    fun loadCategoriesWithRetry(maxRetries: Int = 3, delayMillis: Long = 2000) {
        networkHelper.executeWithRetry(
            scope = viewModelScope,
            operation = { getCategoriesUseCase() },
            onSuccess = { categories ->
                Log.d("CategoryViewModel", "Категории успешно загружены: ${categories.size} элементов")
                _uiState.value = yandex.school.project.core.utils.Result.Success(categories)
            },
            onError = { errorMessage ->
                _uiState.value = yandex.school.project.core.utils.Result.Error(errorMessage)
            },
            maxRetries = maxRetries,
            delayMillis = delayMillis,
            operationName = "загрузка категорий"
        )
    }
} 