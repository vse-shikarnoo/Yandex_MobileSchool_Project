package yandex.school.project.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AccountCreateRequest(
    val name: String,
    val balance: Double,
    val currency: String
) 