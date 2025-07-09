package yandex.school.project.core.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AccountBrief(
    val id: Int,
    val name: String,
    val balance: Double,
    val currency: String
) 