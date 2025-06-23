package yandex.school.project.presentation.screens.category

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import yandex.school.project.domain.entities.Category
import yandex.school.project.domain.usecases.category.GetCategoriesUseCase
import yandex.school.project.presentation.common.Result
import yandex.school.project.presentation.common.BaseNetworkViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : BaseNetworkViewModel() {
    
    private val _uiState = MutableStateFlow<Result<List<Category>>>(Result.Loading)
    val uiState: StateFlow<Result<List<Category>>> = _uiState.asStateFlow()

    init {
        loadCategoriesWithRetry()
    }

    fun loadCategoriesWithRetry(maxRetries: Int = 3, delayMillis: Long = 2000) {
        executeWithRetry(
            operation = { getCategoriesUseCase() },
            onSuccess = { categories ->
                Log.d("CategoryViewModel", "Категории успешно загружены: ${categories.size} элементов")
                _uiState.value = Result.Success(categories)
            },
            onError = { errorMessage ->
                _uiState.value = Result.Error(errorMessage)
            },
            maxRetries = maxRetries,
            delayMillis = delayMillis,
            operationName = "загрузка категорий"
        )
    }
} 