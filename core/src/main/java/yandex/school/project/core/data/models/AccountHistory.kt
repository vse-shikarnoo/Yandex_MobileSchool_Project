package yandex.school.project.core.data.models

import kotlinx.serialization.Serializable

/**
 * Тип изменения аккаунта (создание или модификация).
 */
enum class AccountChangeType {
    CREATION,
    MODIFICATION
}

@Serializable
data class AccountHistory(
    val id: Int,
    val accountId: Int,
    val changeType: AccountChangeType,
    val previousState: AccountState?,
    val newState: AccountState,
    val changeTimestamp: String,
    val createdAt: String
) 