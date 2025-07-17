package yandex.school.project.core.domain.usecases.category

import kotlinx.coroutines.flow.Flow
import yandex.school.project.core.domain.entities.Category
import yandex.school.project.core.domain.repositories.CategoryRepository
import javax.inject.Inject

/**
 * Use case для получения списка всех категорий.
 * Единственная ответственность: получение списка категорий из репозитория.
 */
class GetCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): Flow<List<Category>> {
        return categoryRepository.getCategories()
    }
} 