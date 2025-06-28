package yandex.school.project.domain.usecases.category

import yandex.school.project.domain.entities.Category
import yandex.school.project.domain.repositories.CategoryRepository
import javax.inject.Inject

/**
 * Use case для получения списка всех категорий.
 * Единственная ответственность: получение списка категорий из репозитория.
 */
class GetCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): List<Category> {
        return categoryRepository.getCategories()
    }
} 