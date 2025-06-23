package yandex.school.project.domain.usecases.category

import yandex.school.project.domain.entities.Category
import yandex.school.project.domain.repositories.CategoryRepository
import javax.inject.Inject

class GetCategoriesByTypeUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(isIncome: Boolean): List<Category> {
        return categoryRepository.getCategoriesByType(isIncome)
    }
} 