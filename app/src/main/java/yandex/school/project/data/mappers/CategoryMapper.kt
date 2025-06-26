package yandex.school.project.data.mappers

import yandex.school.project.data.models.Category as DataCategory
import yandex.school.project.domain.entities.Category as DomainCategory

/**
 * Extension-функции для преобразования между data и domain Category.
 */

fun DataCategory.toDomain(): DomainCategory = DomainCategory(
    id = id,
    name = name,
    isIncome = isIncome,
    color = null,
    icon = emoji
)

fun DomainCategory.toData(): DataCategory = DataCategory(
    id = id,
    name = name,
    emoji = icon ?: "",
    isIncome = isIncome
) 