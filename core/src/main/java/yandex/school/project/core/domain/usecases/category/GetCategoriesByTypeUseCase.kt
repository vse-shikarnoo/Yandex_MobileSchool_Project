package yandex.school.project.core.domain.usecases.category

import kotlinx.coroutines.flow.Flow
import yandex.school.project.core.domain.entities.Category
import yandex.school.project.core.domain.repositories.CategoryRepository
import javax.inject.Inject

/**
 * Use case для получения категорий по типу (доходы/расходы).
 * Единственная ответственность: получение списка категорий определенного типа из репозитория.
 */
class GetCategoriesByTypeUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke(isIncome: Boolean): Flow<List<Category>> {
        // Если репозиторий поддерживает только suspend, можно обернуть в flow { emit(...) }
        // return flow { emit(categoryRepository.getCategoriesByType(isIncome)) }
        throw NotImplementedError("Репозиторий не поддерживает Flow для getCategoriesByType")
    }
} 