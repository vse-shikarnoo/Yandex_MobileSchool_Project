package yandex.school.project.core.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AccountUpdateRequest(
    val name: String,
    val balance: Double,
    val currency: String
) 