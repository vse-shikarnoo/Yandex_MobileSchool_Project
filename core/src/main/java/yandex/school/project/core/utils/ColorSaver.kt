package yandex.school.project.core.utils

import android.util.Log
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.ui.graphics.Color
import yandex.school.project.core.theme.GreenLight
import yandex.school.project.core.ui.common.ThemeColors

fun safeColorFromLong(value: Long): Color {
    Log.d("ColorSaver", "Попытка восстановить цвет из long: $value (hex: ${value.toULong().toString(16)})")
    return try {
        Color(value.toULong())
    } catch (e: Exception) {
        Log.e("ColorSaver", "Ошибка восстановления цвета из long: $value (hex: ${value.toULong().toString(16)})", e)
        GreenLight
    }
}

val ColorSaver = Saver<Color, Long>(
    save = { it.value.toLong() },
    restore = { safeColorFromLong(it) }
)

val ThemeColorsSaver = listSaver<ThemeColors, Long>(
    save = { listOf(it.primary.value.toLong(), it.secondary.value.toLong()) },
    restore = {
        ThemeColors(
            if (it[0] == 0L) GreenLight else safeColorFromLong(it[0]),
            if (it[1] == 0L) GreenLight else safeColorFromLong(it[1])
        )
    }
)