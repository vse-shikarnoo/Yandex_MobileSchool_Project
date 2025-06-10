package yandex.school.project.domain.models

data class AccountDomain(
    val id: Int,
    val name: String,
    val balance: String, // Лучше BigDecimal, если поддерживается
    val currency: String
) 