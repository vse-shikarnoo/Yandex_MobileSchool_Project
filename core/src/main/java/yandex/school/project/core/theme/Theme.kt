package yandex.school.project.core.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import yandex.school.project.core.data.local.UserPreferences
import android.util.Log

private val DarkColorScheme = darkColorScheme(
    primary = GreenMain,
    secondary = GreenLight,
    tertiary = Grey,
    error = RedMain
)

private val LightColorScheme = lightColorScheme(
    primary = GreenMain,
    secondary = GreenLight,
    tertiary = Grey,
    error = RedMain

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun ProjectTheme(
    userPreferences: UserPreferences? = null,
    darkTheme: Boolean = userPreferences?.darkTheme ?: isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (userPreferences != null) {
        val primary = (userPreferences.primaryColor and 0xFFFFFFFFUL).toLong()
        val secondary = (userPreferences.secondaryColor and 0xFFFFFFFFUL).toLong()
        Log.d("ProjectTheme", "primaryColor ARGB: 0x${primary.toString(16)} secondaryColor ARGB: 0x${secondary.toString(16)}")
        if (darkTheme) {
            darkColorScheme(
                primary = Color(primary),
                secondary = Color(secondary),
                tertiary = Grey,
                error = RedMain
            )
        } else {
            lightColorScheme(
                primary = Color(primary),
                secondary = Color(secondary),
                tertiary = Grey,
                error = RedMain
            )
        }
    } else if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
} 