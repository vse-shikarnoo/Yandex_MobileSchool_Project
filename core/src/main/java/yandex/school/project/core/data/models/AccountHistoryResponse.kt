package yandex.school.project.core.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AccountHistoryResponse(
    val accountId: Int,
    val accountName: String,
    val currency: String,
    val currentBalance: String,
    val history: List<AccountHistory>
) 