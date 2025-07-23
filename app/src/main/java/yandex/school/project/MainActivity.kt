package yandex.school.project

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import yandex.school.project.core.theme.GreenLight
import yandex.school.project.core.theme.ProjectTheme
import yandex.school.project.core.ui.common.ThemeColors
import yandex.school.project.core.utils.ThemeColorsSaver
import yandex.school.project.core.utils.ThemeSettingsStore
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
            val context = LocalContext.current
            val themeSettingsFlow = remember { ThemeSettingsStore.themeSettingsFlow(context) }
            val themeSettings by themeSettingsFlow.collectAsState(initial = ThemeSettingsStore.ThemeSettings())
            var darkTheme by rememberSaveable { mutableStateOf(themeSettings.darkTheme) }

            var themeColors by rememberSaveable(stateSaver = ThemeColorsSaver) {
                mutableStateOf(
                    ThemeColors(
                        if (themeSettings.primaryColor == 0L) GreenLight else Color(themeSettings.primaryColor),
                        if (themeSettings.secondaryColor == 0L) GreenLight else Color(themeSettings.secondaryColor)
                    )
                )
            }
            var hapticsEnabled by rememberSaveable { mutableStateOf(themeSettings.hapticsEnabled) }
            var isUserChangingColors by remember { mutableStateOf(false) }

            // Синхронизация состояния с DataStore
            LaunchedEffect(themeSettings) {
                Log.d("MainActivity", "[ThemeSettings] darkTheme: ${themeSettings.darkTheme}, primaryColor: ${themeSettings.primaryColor}, secondaryColor: ${themeSettings.secondaryColor}, haptics: ${themeSettings.hapticsEnabled}")
                Log.d("MainActivity", "[До преобразования] themeColors.primary: ${themeColors.primary.value}, themeColors.secondary: ${themeColors.secondary.value}")

                val newPrimary = if (themeSettings.primaryColor == 0L || themeColors.primary.value == 0UL) GreenLight else Color(themeSettings.primaryColor.toULong())
                val newSecondary = if (themeSettings.secondaryColor == 0L || themeColors.secondary.value == 0UL) GreenLight else Color(themeSettings.secondaryColor.toULong())

                Log.d("MainActivity", "[Преобразовано] newPrimary: ${newPrimary.value}, newSecondary: ${newSecondary.value}")

                if (!isUserChangingColors && (themeColors.primary.value != newPrimary.value || themeColors.secondary.value != newSecondary.value)) {
                    Log.d("MainActivity", "[Обновление themeColors] Было: primary=${themeColors.primary.value}, secondary=${themeColors.secondary.value}; Станет: primary=${newPrimary.value}, secondary=${newSecondary.value}")
                    themeColors = ThemeColors(newPrimary, newSecondary)
                }
                if (darkTheme != themeSettings.darkTheme) darkTheme = themeSettings.darkTheme
                if (hapticsEnabled != themeSettings.hapticsEnabled) hapticsEnabled = themeSettings.hapticsEnabled
            }

            // Сохранять изменения в DataStore
            LaunchedEffect(themeColors) {
                Log.d("MainActivity", "[Сохранение] themeColors.primary: ${themeColors.primary.value}, themeColors.secondary: ${themeColors.secondary.value}")
                if (themeColors.primary.value != 0UL) {
                    Log.d("MainActivity", "[Сохраняю primaryColor] value: ${themeColors.primary.value.toLong()}")
                    ThemeSettingsStore.setPrimaryColor(context, themeColors.primary.value.toLong())
                } else {
                    Log.d("MainActivity", "[Не сохраняю primaryColor] value == 0")
                }
                if (themeColors.secondary.value != 0UL) {
                    Log.d("MainActivity", "[Сохраняю secondaryColor] value: ${themeColors.secondary.value.toLong()}")
                    ThemeSettingsStore.setSecondaryColor(context, themeColors.secondary.value.toLong())
                } else {
                    Log.d("MainActivity", "[Не сохраняю secondaryColor] value == 0")
                }
                isUserChangingColors = false
            }
            LaunchedEffect(hapticsEnabled) {
                ThemeSettingsStore.setHapticsEnabled(context, hapticsEnabled)
            }

            ProjectTheme(
                darkTheme = darkTheme,
                primaryColor = if (themeColors.primary.value == 0UL) GreenLight else themeColors.primary,
                secondaryColor = if (themeColors.secondary.value == 0UL) GreenLight else themeColors.secondary
            ) {
                AppNavigation(
                    onThemeChange = { darkTheme = it },
                    onColorsChange = {
                        isUserChangingColors = true
                        themeColors = it
                    },
                    darkTheme = darkTheme,
                    primaryColor = if (themeColors.primary.value == 0UL) GreenLight else themeColors.primary,
                    secondaryColor = if (themeColors.secondary.value == 0UL) GreenLight else themeColors.secondary,
                    hapticsEnabled = hapticsEnabled,
                    onHapticsChange = { hapticsEnabled = it }
                )
            }
        }
    }
}