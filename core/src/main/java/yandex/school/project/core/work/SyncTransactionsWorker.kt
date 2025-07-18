package yandex.school.project.core.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import yandex.school.project.core.data.repository.AccountRepositoryImpl
import yandex.school.project.core.data.repository.TransactionRepositoryImpl

class SyncTransactionsWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val transactionRepository: TransactionRepositoryImpl,
    private val accountRepository: AccountRepositoryImpl
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("SyncTransactionsWorker", "doWork: старт синхронизации")
        return try {
            transactionRepository.syncTransactions()
            accountRepository.syncAccounts()
            Log.d("SyncTransactionsWorker", "doWork: синхронизация завершена успешно")
            Result.success()
        } catch (e: Exception) {
            Log.e("SyncTransactionsWorker", "doWork: ошибка синхронизации", e)
            Result.retry()
        }
    }
}