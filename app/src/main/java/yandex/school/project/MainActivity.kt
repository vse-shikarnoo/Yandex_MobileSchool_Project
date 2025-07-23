package yandex.school.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelProvider
import yandex.school.project.core.theme.ProjectTheme
import yandex.school.project.presentation.navigation.AppNavigation
import yandex.school.project.core.data.local.UserPreferencesDataStore
import yandex.school.project.core.data.local.UserPreferences

/**
 * Главная активность приложения, отвечающая за запуск UI и навигации.
 * Единственная ответственность: инициализация и отображение основного интерфейса приложения.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val dataStore = remember { UserPreferencesDataStore(this) }
            val userPrefs by dataStore.preferencesFlow.collectAsState(initial = UserPreferences(0xFF2AE881UL, 0xFFD4FAE6UL, false))
            ProjectTheme(userPreferences = userPrefs) {
                AppNavigation()
            }
        }
    }
}