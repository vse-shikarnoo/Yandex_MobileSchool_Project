package yandex.school.project.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import yandex.school.project.core.data.local.dao.CategoryDao
import yandex.school.project.core.data.mappers.toData
import yandex.school.project.core.data.mappers.toDomain
import yandex.school.project.core.data.mappers.toEntity
import yandex.school.project.core.data.network.ApiService
import yandex.school.project.core.domain.entities.Category
import yandex.school.project.core.domain.repositories.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override suspend fun getCategories(): Flow<List<Category>> {
        return categoryDao.getAll().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getCategoriesByType(isIncome: Boolean): List<Category> {
        TODO("Not yet implemented")
    }
}