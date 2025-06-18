package yandex.school.project.data.repository

import yandex.school.project.data.network.ApiService
import yandex.school.project.data.models.TransactionResponse

class IncomeRepository(private val apiService: ApiService) {
    
    suspend fun getIncomeTransactions(accountId: Int, startDate: String? = "2025-06-18", endDate: String? = "2025-06-18"): List<TransactionResponse> {
        val allTransactions = apiService.getTransactionsByAccountPeriod(accountId, startDate, endDate)
        return allTransactions.filter { it.category.isIncome }
    }
} 