package yandex.school.project.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import yandex.school.project.core.data.local.AppDatabase
import yandex.school.project.core.data.local.dao.AccountDao
import yandex.school.project.core.data.local.dao.AccountHistoryDao
import yandex.school.project.core.data.local.dao.CategoryDao
import yandex.school.project.core.data.local.dao.TransactionDao

@Module
object RoomModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "finance_app_db"
        ).build()

    @Provides
    fun provideTransactionDao(db: AppDatabase): TransactionDao = db.transactionDao()

    @Provides
    fun provideAccountDao(db: AppDatabase): AccountDao = db.accountDao()

    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao()

    @Provides
    fun provideAccountHistoryDao(db: AppDatabase): AccountHistoryDao = db.accountHistoryDao()
} 