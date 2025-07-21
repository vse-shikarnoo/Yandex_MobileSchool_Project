package yandex.school.project.core.data.mappers

import yandex.school.project.core.data.local.entities.CategoryEntity
import yandex.school.project.core.data.models.Category as DataCategory
import yandex.school.project.core.domain.entities.Category as DomainCategory

/**
 * Функции для преобразования объектов Category между слоями данных и домена.
 * Единственная ответственность: преобразование данных категорий между различными слоями приложения.
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

fun CategoryEntity.toDomain(): DomainCategory = DomainCategory(
    id = id,
    name = name,
    isIncome = type == "INCOME", // или другой способ определения
    icon = emoji
)

fun DomainCategory.toEntity(isSynced: Boolean = false): CategoryEntity = CategoryEntity(
    id = id,
    name = name,
    emoji = icon ?: "",
    type = if (isIncome) "INCOME" else "EXPENSE",
    isSynced = isSynced
)