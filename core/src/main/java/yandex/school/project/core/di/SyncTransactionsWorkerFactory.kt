package yandex.school.project.core.di

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import yandex.school.project.core.data.repository.AccountRepositoryImpl
import yandex.school.project.core.data.repository.TransactionRepositoryImpl
import yandex.school.project.core.work.SyncTransactionsWorker
import javax.inject.Inject
import javax.inject.Provider

class SyncTransactionsWorkerFactory @Inject constructor(
    private val transactionRepositoryProvider: Provider<TransactionRepositoryImpl>,
    private val accountRepositoryProvider: Provider<AccountRepositoryImpl>
) {
    fun create(context: Context, params: WorkerParameters): SyncTransactionsWorker {
        return SyncTransactionsWorker(context, params, transactionRepositoryProvider.get(), accountRepositoryProvider.get())
    }
}