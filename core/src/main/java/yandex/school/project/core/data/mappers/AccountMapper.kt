package yandex.school.project.core.data.mappers

import yandex.school.project.core.data.local.entities.AccountEntity
import yandex.school.project.core.data.models.AccountCreateRequest
import yandex.school.project.core.data.models.Account as DataAccount
import yandex.school.project.core.data.models.AccountResponse as DataAccountResponse
import yandex.school.project.core.data.models.AccountHistoryResponse
import yandex.school.project.core.data.models.AccountHistory as DataAccountHistory
import yandex.school.project.core.data.models.AccountState
import yandex.school.project.core.domain.entities.Account as DomainAccount
import yandex.school.project.core.domain.entities.AccountHistory as DomainAccountHistory
import yandex.school.project.core.domain.entities.Transaction as DomainTransaction
import yandex.school.project.core.domain.entities.TransactionType


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

fun AccountEntity.toDomain(): DomainAccount = DomainAccount(
    id = id,
    userId = userId,
    name = name,
    balance = balance,
    currency = currency,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun DomainAccount.toEntity(isSynced: Boolean = false, syncAction: String?): AccountEntity =
    AccountEntity(
        id = id,
        userId = userId,
        name = name,
        balance = balance,
        currency = currency,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isSynced = isSynced,
        syncAction = syncAction
    )

fun DataAccount.toCreateRequest(): AccountCreateRequest = AccountCreateRequest(
    name = name,
    balance = balance,
    currency = currency
)

fun AccountHistoryResponse.toDomain(): DomainAccountHistory = DomainAccountHistory(
    accountId = accountId,
    transactions = history.mapIndexed { i, h ->
        DomainTransaction(
            id = h.id,
            accountId = h.accountId,
            categoryId = 0, // История не содержит категорий
            amount = h.newState.balance - (h.previousState?.balance ?: 0.0),
            description = null,
            date = h.changeTimestamp,
            type = if ((h.newState.balance - (h.previousState?.balance ?: 0.0)) >= 0) TransactionType.INCOME else TransactionType.EXPENSE,
            createdAt = h.createdAt,
            updatedAt = h.changeTimestamp
        )
    },
    totalIncome = history.sumOf { (it.newState.balance - (it.previousState?.balance ?: 0.0)).takeIf { d -> d > 0 } ?: 0.0 },
    totalExpense = history.sumOf { (it.newState.balance - (it.previousState?.balance ?: 0.0)).takeIf { d -> d < 0 } ?: 0.0 },
    balance = currentBalance.toDoubleOrNull() ?: 0.0
)