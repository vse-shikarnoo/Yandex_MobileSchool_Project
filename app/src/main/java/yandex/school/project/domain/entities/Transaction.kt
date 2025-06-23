package yandex.school.project.domain.entities

data class Transaction(
    val id: Int,
    val accountId: Int,
    val categoryId: Int,
    val amount: Double,
    val description: String?,
    val date: String,
    val type: TransactionType,
    val createdAt: String,
    val updatedAt: String
)

enum class TransactionType {
    INCOME,
    EXPENSE
} 