package yandex.school.project.data.models

import kotlinx.serialization.Serializable

/**
 * DTO категории для работы с сетью/БД.
 */
@Serializable
data class Category(
    val id: Int,
    val name: String,
    val emoji: String,
    val isIncome: Boolean
) 