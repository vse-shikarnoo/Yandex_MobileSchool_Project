package yandex.school.project.domain.models

data class AccountHistoryDomain(
    val id: Int,
    val accountId: Int,
    val changeType: String,
    val previousState: AccountStateDomain?,
    val newState: AccountStateDomain,
    val changeTimestamp: String,
    val createdAt: String
) 