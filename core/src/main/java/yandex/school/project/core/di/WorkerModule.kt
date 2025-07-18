package yandex.school.project.core.di

import dagger.Module
import dagger.Provides
import yandex.school.project.core.data.repository.AccountRepositoryImpl
import yandex.school.project.core.data.repository.TransactionRepositoryImpl
import yandex.school.project.core.work.SyncTransactionsWorker
import javax.inject.Provider
import javax.inject.Singleton

@Module
object WorkerModule {
    @Provides
    @Singleton
    fun provideSyncTransactionsWorkerFactory(
        transactionRepositoryProvider: Provider<TransactionRepositoryImpl>,
        accountRepositoryProvider: Provider<AccountRepositoryImpl>
    ): SyncTransactionsWorkerFactory {
        return SyncTransactionsWorkerFactory(transactionRepositoryProvider, accountRepositoryProvider)
    }
}