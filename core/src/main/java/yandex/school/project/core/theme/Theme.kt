package yandex.school.project.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

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
    darkTheme: Boolean = isSystemInDarkTheme(),
    primaryColor: Color = GreenMain,
    secondaryColor: Color = GreenLight,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = primaryColor,
            secondary = secondaryColor,
            tertiary = Grey,
            error = RedMain
        )
    } else {
        lightColorScheme(
            primary = primaryColor,
            secondary = secondaryColor,
            tertiary = Grey,
            error = RedMain
        )
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
} 