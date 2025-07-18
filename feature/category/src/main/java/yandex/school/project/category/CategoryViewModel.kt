package yandex.school.project.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import yandex.school.project.core.domain.entities.Category
import yandex.school.project.core.domain.usecases.category.GetCategoriesUseCase
import yandex.school.project.core.utils.Result
import javax.inject.Inject

/**
 * ViewModel для экрана категорий, управляющий состоянием и загрузкой списка категорий.
 * Единственная ответственность: управление состоянием UI и загрузка списка всех категорий.
 */
class CategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<Result<List<Category>>>(Result.Loading)
    val uiState: StateFlow<Result<List<Category>>> = _uiState.asStateFlow()

    init {
        observeCategories()
    }

    fun observeCategories() {
        viewModelScope.launch {
            getCategoriesUseCase()
                .catch { e -> _uiState.value = Result.Error(e.message ?: "Ошибка загрузки категорий") }
                .collect { categories ->
                    Log.d("CategoryViewModel", "Категории успешно загружены: ${categories.size} элементов")
                    _uiState.value = Result.Success(categories)
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("${this::class.java}", "onCleared: ")
    }
}