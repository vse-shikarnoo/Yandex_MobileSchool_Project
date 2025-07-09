package yandex.school.project.core.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AccountResponse(
    val id: Int,
    val name: String,
    val balance: Double,
    val currency: String,
    val incomeStats: List<yandex.school.project.core.data.models.StatItem>,
    val expenseStats: List<yandex.school.project.core.data.models.StatItem>,
    val createdAt: String,
    val updatedAt: String
) 