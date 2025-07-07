package yandex.school.project.domain.repositories

import yandex.school.project.domain.entities.Category

interface CategoryRepository {
    suspend fun getCategories(): List<Category>
    suspend fun getCategoriesByType(isIncome: Boolean): List<Category>
} 