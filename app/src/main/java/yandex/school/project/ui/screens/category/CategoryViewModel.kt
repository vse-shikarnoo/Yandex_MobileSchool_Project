package yandex.school.project.ui.screens.category

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import yandex.school.project.domain.models.CategoryDomain

class CategoryViewModel : ViewModel() {
    private val _categories = MutableStateFlow<List<CategoryDomain>>(emptyList())
    val categories: StateFlow<List<CategoryDomain>> = _categories.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        // Здесь в будущем будет загрузка данных из репозитория
        // Сейчас используем тестовые данные
        val mockCategories = listOf(
            CategoryDomain(1, "Аренда квартиры", "🏡", false),
            CategoryDomain(2, "Одежда", "👗", false),
            CategoryDomain(3, "На собачку", "🐶", false),
            CategoryDomain(4, "Ремонт квартиры", "PK", false),
            CategoryDomain(5, "Продукты", "🍭", false),
            CategoryDomain(6, "Спортзал", "🏋️", false),
            CategoryDomain(7, "Медицина", "💊", false)
        )
        _categories.value = mockCategories
    }
}