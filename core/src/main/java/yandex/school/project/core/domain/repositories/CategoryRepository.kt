package yandex.school.project.core.domain.repositories

import kotlinx.coroutines.flow.Flow
import yandex.school.project.core.domain.entities.Category

interface CategoryRepository {
    suspend fun getCategories(): Flow<List<Category>>
    suspend fun getCategoriesByType(isIncome: Boolean): List<Category>
} 