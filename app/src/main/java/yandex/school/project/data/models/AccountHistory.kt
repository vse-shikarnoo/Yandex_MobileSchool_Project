package yandex.school.project.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AccountHistory(
    val id: Int,
    val accountId: Int,
    val changeType: String, // CREATION или MODIFICATION
    val previousState: AccountState?,
    val newState: AccountState,
    val changeTimestamp: String,
    val createdAt: String
) 