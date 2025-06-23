package yandex.school.project.domain.entities

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