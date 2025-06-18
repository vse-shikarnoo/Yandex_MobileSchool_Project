package yandex.school.project.ui.screens.category

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import yandex.school.project.data.models.Category
import yandex.school.project.data.repository.CategoryRepository
import yandex.school.project.data.network.ApiService
import yandex.school.project.data.network.ApiClient

class CategoryViewModel : ViewModel() {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val repository = CategoryRepository(ApiService(ApiClient()))

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                val result = repository.getCategories()
                _categories.value = result
            } catch (e: Exception) {
                // TODO: обработка ошибки
                _categories.value = emptyList()
            }
        }
    }
}