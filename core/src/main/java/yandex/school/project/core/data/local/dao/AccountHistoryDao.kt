package yandex.school.project.core.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import yandex.school.project.core.data.local.entities.AccountHistoryEntity

@Dao
interface AccountHistoryDao {
    @Query("SELECT * FROM account_history")
    fun getAll(): Flow<List<AccountHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: AccountHistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(histories: List<AccountHistoryEntity>)

    @Update
    suspend fun update(history: AccountHistoryEntity)

    @Delete
    suspend fun delete(history: AccountHistoryEntity)

    @Query("SELECT * FROM account_history WHERE isSynced = 0")
    suspend fun getUnsynced(): List<AccountHistoryEntity>
} 