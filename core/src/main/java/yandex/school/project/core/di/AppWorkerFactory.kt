package yandex.school.project.core.di

import android.content.Context
import androidx.work.WorkerFactory
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import yandex.school.project.core.work.SyncTransactionsWorker

class AppWorkerFactory(
    private val syncTransactionsWorkerFactory: SyncTransactionsWorkerFactory
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            SyncTransactionsWorker::class.java.name ->
                syncTransactionsWorkerFactory.create(appContext, workerParameters)
            else -> null
        }
    }
}