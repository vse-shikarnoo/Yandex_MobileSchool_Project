package yandex.school.project.core.domain.entities

data class AccountHistory(
    val accountId: Int,
    val transactions: List<Transaction>,
    val totalIncome: Double,
    val totalExpense: Double,
    val balance: Double
) 