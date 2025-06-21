package yandex.school.project.data.repository

import yandex.school.project.data.models.Account
import yandex.school.project.data.models.AccountResponse
import yandex.school.project.data.network.ApiService

class AccountRepository(private val apiService: ApiService) {
    suspend fun getAccounts(): List<Account> {
        return apiService.getAccounts()
    }

    suspend fun getAccountById(id: Int): AccountResponse {
        return apiService.getAccountById(id)
    }
} 