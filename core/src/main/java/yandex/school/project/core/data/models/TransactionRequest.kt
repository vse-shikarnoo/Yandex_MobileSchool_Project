package yandex.school.project.core.data.models

import kotlinx.serialization.Serializable

/**
 * DTO для создания/обновления транзакции (отправляется на сервер).
 */
@Serializable
data class TransactionRequest(
    val accountId: Int,
    val categoryId: Int,
    val amount: Double,
    val transactionDate: String,
    val comment: String?
) 