package yandex.school.project.data.models

import kotlinx.serialization.Serializable

/**
 * DTO аккаунта для работы с сетью/БД.
 */
@Serializable
data class Account(
    val id: Int,
    val userId: Int,
    val name: String,
    val balance: Double,
    val currency: String,
    val createdAt: String,
    val updatedAt: String
) 