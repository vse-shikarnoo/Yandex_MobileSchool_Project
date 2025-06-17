package yandex.school.project.ui.screens.category

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import yandex.school.project.data.models.Category

class CategoryViewModel : ViewModel() {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        // Здесь в будущем будет загрузка данных из репозитория
        // Сейчас используем тестовые данные
        val mockCategories = listOf(
            Category(1, "Аренда квартиры", "🏡", false),
            Category(2, "Одежда", "👗", false),
            Category(3, "На собачку", "🐶", false),
            Category(4, "Ремонт квартиры", "PK", false),
            Category(5, "Продукты", "🍭", false),
            Category(6, "Спортзал", "🏋️", false),
            Category(7, "Медицина", "💊", false)
        )
        _categories.value = mockCategories
    }
}