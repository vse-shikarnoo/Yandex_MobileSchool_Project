package yandex.school.project.domain.models

data class TransactionDomain(
    val id: Int,
    val accountId: Int,
    val categoryId: Int,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
) 