package yandex.school.project.core.domain.entities

/**
 * Доменная сущность категории для бизнес-логики приложения.
 */
data class Category(
    val id: Int,
    val name: String,
    val isIncome: Boolean,
    val color: String? = null,
    val icon: String? = null
) 