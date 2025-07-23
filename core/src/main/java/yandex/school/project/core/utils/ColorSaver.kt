package yandex.school.project.core.utils

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.ui.graphics.Color
import yandex.school.project.core.theme.GreenLight
import yandex.school.project.core.ui.common.ThemeColors


val ColorSaver = Saver<Color, Long>(
    save = { it.value.toLong() },
    restore = { Color(it.toULong()) }
)

val ThemeColorsSaver = listSaver<ThemeColors, Long>(
    save = { listOf(it.primary.value.toLong(), it.secondary.value.toLong()) },
    restore = {
        ThemeColors(
            if (it[0] == 0L) GreenLight else Color(it[0].toULong()),
            if (it[1] == 0L) GreenLight else Color(it[1].toULong())
        )
    }
)