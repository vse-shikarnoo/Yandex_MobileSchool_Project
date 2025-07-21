package yandex.school.project

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import yandex.school.project.core.di.AppWorkerFactory
import yandex.school.project.di.AppComponent
import yandex.school.project.di.DaggerAppComponent

/**
 * Главный класс приложения, отвечающий за инициализацию Hilt для dependency injection.
 * Единственная ответственность: настройка и запуск системы dependency injection.
 */
class FinanceApplication : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(applicationContext)
        val workerFactory = AppWorkerFactory(appComponent.syncTransactionsWorkerFactory())
        WorkManager.initialize(
            this,
            Configuration.Builder().setWorkerFactory(workerFactory).build()
        )
        NetworkSyncHelper.registerNetworkCallback(this)
    }
}