package yandex.school.project.core.domain.entities

/**
 * Доменная сущность аккаунта для бизнес-логики приложения.
 */
data class Account(
    val id: Int,
    val userId: Int = 0,
    val name: String,
    val balance: Double,
    val currency: String,
    val isActive: Boolean = true,
    val createdAt: String,
    val updatedAt: String
) 