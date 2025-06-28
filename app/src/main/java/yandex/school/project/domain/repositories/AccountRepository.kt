package yandex.school.project.domain.repositories

import yandex.school.project.domain.entities.Account
import yandex.school.project.domain.entities.AccountHistory

interface AccountRepository {
    suspend fun getAccounts(): List<Account>
    suspend fun getAccountById(id: Int): Account
    suspend fun createAccount(name: String, balance: Double, currency: String): Account
    suspend fun updateAccount(id: Int, name: String, balance: Double, currency: String): Account
    suspend fun deleteAccount(id: Int)
    suspend fun getAccountHistory(id: Int): AccountHistory
} 