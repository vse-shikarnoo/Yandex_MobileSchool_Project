package yandex.school.project.data.mappers

import yandex.school.project.data.models.Category as DataCategory
import yandex.school.project.domain.entities.Category as DomainCategory

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