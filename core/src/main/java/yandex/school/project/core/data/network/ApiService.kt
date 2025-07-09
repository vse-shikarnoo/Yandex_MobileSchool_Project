package yandex.school.project.core.data.network

import android.util.Log
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import yandex.school.project.core.data.models.*
import java.io.IOException

/**
 * Сервис для выполнения HTTP запросов к API серверу.
 * Единственная ответственность: выполнение сетевых запросов и получение данных от API.
 */
class ApiService(private val apiClient: ApiClient) {

    // Accounts
    suspend fun getAccounts(): List<Account> = withContext(Dispatchers.IO) {
        Log.d("ApiService", "Отправка GET запроса на: ${ApiEndpoints.ACCOUNTS}")
        val response = apiClient.client.get(ApiEndpoints.ACCOUNTS)
        Log.d("ApiService", "Получен ответ с кодом: ${response.status}")
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

    suspend fun updateAccount(id: Int, accountData: yandex.school.project.core.data.models.AccountUpdateRequest): Account = withContext(Dispatchers.IO) {
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
        try {
            Log.d("ApiService", "Отправка GET запроса на: ${ApiEndpoints.CATEGORIES}")
            val response = apiClient.client.get(ApiEndpoints.CATEGORIES)
            Log.d("ApiService", "Получен ответ с кодом: ${response.status}")
            
            // Проверяем HTTP-код ответа
            if (!response.status.isSuccess()) {
                val errorBody = response.bodyAsText()
                Log.e("ApiService", "HTTP ошибка ${response.status}: $errorBody")
                throw IOException("HTTP ${response.status.value}: ${response.status.description}")
            }
            
            val categories = response.body<List<Category>>()
            Log.d("ApiService", "Успешно десериализовано категорий: ${categories.size}")
            categories
        } catch (e: Exception) {
            Log.e("ApiService", "Ошибка в getCategories: ${e.javaClass.simpleName} - ${e.message}")
            Log.e("ApiService", "Полный стек ошибки:", e)
            throw e
        }
    }

    suspend fun getCategoriesByType(isIncome: Boolean): List<Category> = withContext(Dispatchers.IO) {
        val response = apiClient.client.get(ApiEndpoints.CATEGORIES_BY_TYPE.replace("{isIncome}", isIncome.toString()))
        response.body()
    }

    // Transactions
    suspend fun createTransaction(transactionData: yandex.school.project.core.data.models.TransactionRequest): TransactionResponse = withContext(Dispatchers.IO) {
        val response = apiClient.client.post(ApiEndpoints.TRANSACTIONS) {
            setBody(transactionData)
        }
        response.body()
    }

    suspend fun getTransactionById(id: Int): TransactionResponse = withContext(Dispatchers.IO) {
        val response = apiClient.client.get(ApiEndpoints.TRANSACTION_BY_ID.replace("{id}", id.toString()))
        response.body()
    }

    suspend fun updateTransaction(id: Int, transactionData: yandex.school.project.core.data.models.TransactionRequest): TransactionResponse = withContext(Dispatchers.IO) {
        val response = apiClient.client.put(ApiEndpoints.TRANSACTION_BY_ID.replace("{id}", id.toString())) {
            setBody(transactionData)
        }
        response.body()
    }

    suspend fun deleteTransaction(id: Int): Unit = withContext(Dispatchers.IO) {
        apiClient.client.delete(ApiEndpoints.TRANSACTION_BY_ID.replace("{id}", id.toString()))
    }

    suspend fun getTransactionsByAccountPeriod(
        accountId: Int = 1,
        startDate: String? = null,
        endDate: String? = null
    ): List<TransactionResponse> = withContext(Dispatchers.IO) {
        Log.d("ApiService", "Отправка GET запроса на: ${ApiEndpoints.TRANSACTIONS_BY_ACCOUNT_PERIOD}")
        Log.d("ApiService", "$accountId $startDate $endDate")
        val url = ApiEndpoints.TRANSACTIONS_BY_ACCOUNT_PERIOD
            .replace("{accountId}", accountId.toString())
        
        val response = apiClient.client.get(url) {
            startDate?.let { parameter("startDate", it) }
            endDate?.let { parameter("endDate", it) }
        }
        Log.d("ApiService", "Получен ответ с кодом: ${response.status}")
        response.body()
    }
} 