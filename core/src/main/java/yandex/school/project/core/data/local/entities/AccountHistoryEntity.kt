package yandex.school.project.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_history")
data class AccountHistoryEntity(
    @PrimaryKey val id: Int,
    val accountId: Int,
    val change: Double,
    val date: String,
    val isSynced: Boolean = false
) 