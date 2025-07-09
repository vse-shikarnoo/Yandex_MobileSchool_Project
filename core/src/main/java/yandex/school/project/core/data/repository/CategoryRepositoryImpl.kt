package yandex.school.project.core.data.repository

import yandex.school.project.core.data.mappers.toDomain
import yandex.school.project.core.data.network.ApiService
import yandex.school.project.core.domain.entities.Category
import yandex.school.project.core.domain.repositories.CategoryRepository
import javax.inject.Inject

/**
 * Реализация репозитория для работы с категориями, связывающая домен с данными.
 * Единственная ответственность: управление данными категориями и преобразование между слоями данных.
 */
class CategoryRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : CategoryRepository {
    
    override suspend fun getCategories(): List<Category> {
        return apiService.getCategories().map { it.toDomain() }
    }
    
    override suspend fun getCategoriesByType(isIncome: Boolean): List<Category> {
        return apiService.getCategoriesByType(isIncome).map { it.toDomain() }
    }
} 