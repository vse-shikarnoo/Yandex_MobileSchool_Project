package yandex.school.project.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val accountId: Int,
    val categoryId: Int,
    val amount: Double,
    val description: String?,
    val date: String,
    val type: String,
    val createdAt: String,
    val updatedAt: String,
    val isSynced: Boolean = false
) 