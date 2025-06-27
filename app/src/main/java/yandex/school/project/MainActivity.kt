package yandex.school.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import yandex.school.project.presentation.navigation.AppNavigation
import yandex.school.project.presentation.theme.ProjectTheme

/**
 * Главная активность приложения, отвечающая за запуск UI и навигации.
 * Единственная ответственность: инициализация и отображение основного интерфейса приложения.
 */
@AndroidEntryPoint
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