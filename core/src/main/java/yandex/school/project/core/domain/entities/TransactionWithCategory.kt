package yandex.school.project.core.domain.entities

/**
 * Сущность для отображения транзакции с категорией (используется для истории расходов и доходов).
 */
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