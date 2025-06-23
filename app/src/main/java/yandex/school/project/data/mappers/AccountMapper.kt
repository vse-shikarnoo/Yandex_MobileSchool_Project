package yandex.school.project.data.mappers

import yandex.school.project.data.models.Account as DataAccount
import yandex.school.project.data.models.AccountResponse as DataAccountResponse
import yandex.school.project.domain.entities.Account as DomainAccount

object AccountMapper {
    fun mapToDomain(dataAccount: DataAccount): DomainAccount {
        return DomainAccount(
            id = dataAccount.id,
            userId = dataAccount.userId,
            name = dataAccount.name,
            balance = dataAccount.balance.toDoubleOrNull() ?: 0.0,
            currency = dataAccount.currency,
            isActive = true, // По умолчанию считаем активным
            createdAt = dataAccount.createdAt,
            updatedAt = dataAccount.updatedAt
        )
    }
    
    fun mapToDomain(dataAccountResponse: DataAccountResponse): DomainAccount {
        return DomainAccount(
            id = dataAccountResponse.id,
            userId = 0, // В AccountResponse нет userId
            name = dataAccountResponse.name,
            balance = dataAccountResponse.balance.toDoubleOrNull() ?: 0.0,
            currency = dataAccountResponse.currency,
            isActive = true, // По умолчанию считаем активным
            createdAt = dataAccountResponse.createdAt,
            updatedAt = dataAccountResponse.updatedAt
        )
    }
    
    fun mapToData(domainAccount: DomainAccount): DataAccount {
        return DataAccount(
            id = domainAccount.id,
            userId = domainAccount.userId,
            name = domainAccount.name,
            balance = domainAccount.balance.toString(),
            currency = domainAccount.currency,
            createdAt = domainAccount.createdAt,
            updatedAt = domainAccount.updatedAt
        )
    }
} 