package yandex.school.project

import android.app.Application
import yandex.school.project.di.AppComponent
import yandex.school.project.di.DaggerAppComponent

/**
 * Главный класс приложения, отвечающий за инициализацию Hilt для dependency injection.
 * Единственная ответственность: настройка и запуск системы dependency injection.
 */
class FinanceApplication : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }
} 