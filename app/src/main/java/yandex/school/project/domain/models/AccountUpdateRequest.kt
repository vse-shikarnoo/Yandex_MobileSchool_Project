package yandex.school.project.domain.models

data class AccountUpdateRequest(
    val name: String,
    val balance: String,
    val currency: String
) 