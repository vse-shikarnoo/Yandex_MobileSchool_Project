package yandex.school.project.core.data.models

import kotlinx.serialization.Serializable

@Serializable
data class TransactionCreateResponse(
    val id: Int,
    val accountId: Int,
    val categoryId: Int,
    val amount: Double,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
)