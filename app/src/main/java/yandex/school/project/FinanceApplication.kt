package yandex.school.project

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Главный класс приложения, отвечающий за инициализацию Hilt для dependency injection.
 * Единственная ответственность: настройка и запуск системы dependency injection.
 */
@HiltAndroidApp
class FinanceApplication : Application() 