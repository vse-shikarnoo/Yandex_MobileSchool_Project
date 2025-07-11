package yandex.school.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModelProvider
import yandex.school.project.core.theme.ProjectTheme
import yandex.school.project.presentation.navigation.AppNavigation

/**
 * Главная активность приложения, отвечающая за запуск UI и навигации.
 * Единственная ответственность: инициализация и отображение основного интерфейса приложения.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectTheme {
                AppNavigation()
            }

        }
    }
}