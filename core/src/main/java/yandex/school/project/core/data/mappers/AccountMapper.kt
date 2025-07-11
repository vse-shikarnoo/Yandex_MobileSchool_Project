package yandex.school.project.core.data.mappers

import yandex.school.project.core.data.models.Account as DataAccount
import yandex.school.project.core.data.models.AccountResponse as DataAccountResponse
import yandex.school.project.core.domain.entities.Account as DomainAccount


/**
 * Функции для преобразования объектов Account между слоями данных и домена.
 * Единственная ответственность: преобразование данных аккаунтов между различными слоями приложения.
 */

fun DataAccount.toDomain(): DomainAccount = DomainAccount(
    id = id,
    userId = userId,
    name = name,
    balance = balance,
    currency = currency,
    isActive = true, // По умолчанию считаем активным
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun DataAccountResponse.toDomain(): DomainAccount = DomainAccount(
    id = id,
    userId = 0, // В AccountResponse нет userId
    name = name,
    balance = balance,
    currency = currency,
    isActive = true, // По умолчанию считаем активным
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun DomainAccount.toData(): DataAccount = DataAccount(
    id = id,
    userId = userId,
    name = name,
    balance = balance,
    currency = currency,
    createdAt = createdAt,
    updatedAt = updatedAt
) 