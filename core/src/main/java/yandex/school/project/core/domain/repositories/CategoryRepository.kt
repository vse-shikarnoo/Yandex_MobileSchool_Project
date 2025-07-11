package yandex.school.project.core.domain.repositories

import yandex.school.project.core.domain.entities.Category

interface CategoryRepository {
    suspend fun getCategories(): List<Category>
    suspend fun getCategoriesByType(isIncome: Boolean): List<Category>
} 