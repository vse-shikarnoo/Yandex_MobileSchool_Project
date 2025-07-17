package yandex.school.project.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import yandex.school.project.core.data.local.dao.AccountDao
import yandex.school.project.core.data.local.dao.AccountHistoryDao
import yandex.school.project.core.data.local.dao.CategoryDao
import yandex.school.project.core.data.local.dao.TransactionDao
import yandex.school.project.core.data.local.entities.AccountEntity
import yandex.school.project.core.data.local.entities.AccountHistoryEntity
import yandex.school.project.core.data.local.entities.CategoryEntity
import yandex.school.project.core.data.local.entities.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        AccountEntity::class,
        CategoryEntity::class,
        AccountHistoryEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun accountHistoryDao(): AccountHistoryDao
} 