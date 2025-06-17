package yandex.school.project.data.network

import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import yandex.school.project.data.models.*

class ApiService(private val apiClient: ApiClient) {

    // Accounts
    suspend fun getAccounts(): List<Account> = withContext(Dispatchers.IO) {
        val response = apiClient.client.get(ApiEndpoints.ACCOUNTS)
        response.body()
    }

    suspend fun createAccount(accountData: AccountCreateRequest): Account = withContext(Dispatchers.IO) {
        val response = apiClient.client.post(ApiEndpoints.ACCOUNTS) {
            setBody(accountData)
        }
        response.body()
    }

    suspend fun getAccountById(id: Int): AccountResponse = withContext(Dispatchers.IO) {
        val response = apiClient.client.get(ApiEndpoints.ACCOUNT_BY_ID.replace("{id}", id.toString()))
        response.body()
    }

    suspend fun updateAccount(id: Int, accountData: AccountUpdateRequest): Account = withContext(Dispatchers.IO) {
        val response = apiClient.client.put(ApiEndpoints.ACCOUNT_BY_ID.replace("{id}", id.toString())) {
            setBody(accountData)
        }
        response.body()
    }

    suspend fun deleteAccount(id: Int): Unit = withContext(Dispatchers.IO) {
        apiClient.client.delete(ApiEndpoints.ACCOUNT_BY_ID.replace("{id}", id.toString()))
    }

    suspend fun getAccountHistory(id: Int): AccountHistoryResponse = withContext(Dispatchers.IO) {
        val response = apiClient.client.get(ApiEndpoints.ACCOUNT_HISTORY.replace("{id}", id.toString()))
        response.body()
    }

    // Categories
    suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        val response = apiClient.client.get(ApiEndpoints.CATEGORIES)
        response.body()
    }

    suspend fun getCategoriesByType(isIncome: Boolean): List<Category> = withContext(Dispatchers.IO) {
        val response = apiClient.client.get(ApiEndpoints.CATEGORIES_BY_TYPE.replace("{isIncome}", isIncome.toString()))
        response.body()
    }

    // Transactions
    suspend fun createTransaction(transactionData: TransactionRequest): Transaction = withContext(Dispatchers.IO) {
        val response = apiClient.client.post(ApiEndpoints.TRANSACTIONS) {
            setBody(transactionData)
        }
        response.body()
    }

    suspend fun getTransactionById(id: Int): TransactionResponse = withContext(Dispatchers.IO) {
        val response = apiClient.client.get(ApiEndpoints.TRANSACTION_BY_ID.replace("{id}", id.toString()))
        response.body()
    }

    suspend fun updateTransaction(id: Int, transactionData: TransactionRequest): TransactionResponse = withContext(Dispatchers.IO) {
        val response = apiClient.client.put(ApiEndpoints.TRANSACTION_BY_ID.replace("{id}", id.toString())) {
            setBody(transactionData)
        }
        response.body()
    }

    suspend fun deleteTransaction(id: Int): Unit = withContext(Dispatchers.IO) {
        apiClient.client.delete(ApiEndpoints.TRANSACTION_BY_ID.replace("{id}", id.toString()))
    }

    suspend fun getTransactionsByAccountPeriod(
        accountId: Int,
        startDate: String? = null,
        endDate: String? = null
    ): List<TransactionResponse> = withContext(Dispatchers.IO) {
        val url = ApiEndpoints.TRANSACTIONS_BY_ACCOUNT_PERIOD
            .replace("{accountId}", accountId.toString())
        
        val response = apiClient.client.get(url) {
            startDate?.let { parameter("startDate", it) }
            endDate?.let { parameter("endDate", it) }
        }
        response.body()
    }
} 