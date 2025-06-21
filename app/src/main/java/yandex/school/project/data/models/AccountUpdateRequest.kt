package yandex.school.project.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AccountUpdateRequest(
    val name: String,
    val balance: String,
    val currency: String
) 