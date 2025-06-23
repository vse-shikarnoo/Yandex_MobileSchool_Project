package yandex.school.project.presentation.screens.income

import yandex.school.project.domain.entities.Category
import yandex.school.project.domain.entities.Transaction

data class TransactionWithCategory(
    val transaction: Transaction,
    val category: Category
) {
    val id: Int get() = transaction.id
    val amount: Double get() = transaction.amount
    val description: String? get() = transaction.description
    val date: String get() = transaction.date
    val categoryName: String get() = category.name
    val categoryIcon: String? get() = category.icon
}

data class IncomesState(
    val transactions: List<TransactionWithCategory> = emptyList(),
    val total: String = "0 ₽"
) 