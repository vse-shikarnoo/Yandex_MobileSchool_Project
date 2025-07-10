package yandex.school.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import yandex.school.project.presentation.navigation.AppNavigation
import yandex.school.project.core.theme.ProjectTheme
import androidx.lifecycle.ViewModelProvider
import yandex.school.project.core.di.ViewModelFactory
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalViewModelFactory = staticCompositionLocalOf<ViewModelProvider.Factory> { error("ViewModelFactory not provided") }

/**
 * Главная активность приложения, отвечающая за запуск UI и навигации.
 * Единственная ответственность: инициализация и отображение основного интерфейса приложения.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as FinanceApplication
        val viewModelFactory = app.appComponent.viewModelFactory()
        setContent {
            CompositionLocalProvider(LocalViewModelFactory provides viewModelFactory) {
                ProjectTheme {
                    AppNavigation()
                }
            }
        }
    }
}