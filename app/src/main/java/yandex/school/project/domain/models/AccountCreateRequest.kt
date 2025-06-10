package yandex.school.project.domain.models

data class AccountCreateRequest(
    val name: String,
    val balance: String,
    val currency: String
) 