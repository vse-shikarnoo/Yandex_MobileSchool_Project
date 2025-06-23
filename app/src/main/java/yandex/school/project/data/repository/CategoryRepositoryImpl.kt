package yandex.school.project.data.repository

import yandex.school.project.data.mappers.CategoryMapper
import yandex.school.project.data.network.ApiService
import yandex.school.project.domain.entities.Category
import yandex.school.project.domain.repositories.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : CategoryRepository {
    
    override suspend fun getCategories(): List<Category> {
        return apiService.getCategories().map { CategoryMapper.mapToDomain(it) }
    }
    
    override suspend fun getCategoriesByType(isIncome: Boolean): List<Category> {
        return apiService.getCategoriesByType(isIncome).map { CategoryMapper.mapToDomain(it) }
    }
} 