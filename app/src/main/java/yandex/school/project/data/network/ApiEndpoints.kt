package yandex.school.project.data.network

/**
 * Объект, содержащий все эндпоинты API для работы с сервером.
 * Единственная ответственность: хранение и предоставление URL эндпоинтов для API запросов.
 */
object ApiEndpoints {
    private const val BASE_PATH = "/api/v1"
    
    // Accounts endpoints
    const val ACCOUNTS = "$BASE_PATH/accounts"
    const val ACCOUNT_BY_ID = "$BASE_PATH/accounts/{id}"
    const val ACCOUNT_HISTORY = "$BASE_PATH/accounts/{id}/history"
    
    // Categories endpoints
    const val CATEGORIES = "$BASE_PATH/categories"
    const val CATEGORIES_BY_TYPE = "$BASE_PATH/categories/type/{isIncome}"
    
    // Transactions endpoints
    const val TRANSACTIONS = "$BASE_PATH/transactions"
    const val TRANSACTION_BY_ID = "$BASE_PATH/transactions/{id}"
    const val TRANSACTIONS_BY_ACCOUNT_PERIOD = "$BASE_PATH/transactions/account/{accountId}/period"
} 