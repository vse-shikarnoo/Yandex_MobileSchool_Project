package yandex.school.project.data.repository

import yandex.school.project.data.models.TransactionResponse
import yandex.school.project.data.network.ApiService

class TransactionsRepository(private val apiService: ApiService) {

    suspend fun getTransactions(
        accountId: Int,
        startDate: String? = null,
        endDate: String? = null,
        isIncome: Boolean
    ): List<TransactionResponse> {
        val allTransactions =
            apiService.getTransactionsByAccountPeriod(accountId, startDate, endDate)
        return allTransactions.filter { it.category.isIncome == isIncome }
    }
} 