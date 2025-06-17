package yandex.school.project.data.models

import kotlinx.serialization.Serializable

@Serializable
data class TransactionResponse(
    val id: Int,
    val account: AccountBrief,
    val category: Category,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
) 