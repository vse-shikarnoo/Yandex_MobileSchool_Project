package yandex.school.project.core.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import yandex.school.project.core.theme.*

val colorOptions = listOf(
    0xFF2AE881UL, // GreenMain
    0xFFD4FAE6UL, // GreenLight
    0xFFE46962UL, // RedMain
    0xFFFCE300UL, // YellowMain
    0xFFC4BFC7UL, // Grey
    0xFFD0BCFFUL, // Purple80
    0xFF6650A4UL, // Purple40
    0xFFEFB8C8UL, // Pink80
    0xFF7D5260UL  // Pink40
)

@Composable
fun ColorPicker(
    selectedColor: ULong,
    onColorSelected: (ThemeColors) -> Unit,
    darkTheme: Boolean
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .horizontalScroll(
                rememberScrollState()
            ),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        colorOptions.forEach { colorULong ->
            val color = Color((colorULong and 0xFFFFFFFFUL).toLong())
            val isSelected = (selectedColor and 0xFFFFFFFFUL) == (colorULong and 0xFFFFFFFFUL)
            // Автоподбор второго цвета: если цвет светлый — второй тёмный, если тёмный — второй светлый
            val secondary = autoPickSecondary(colorULong and 0xFFFFFFFFUL)
            CircleColor(
                color = color,
                selected = isSelected,
                onClick = {
                    onColorSelected(
                        ThemeColors(
                            primary = colorULong and 0xFFFFFFFFUL,
                            secondary = secondary
                        )
                    )
                },
                darkTheme = darkTheme
            )
        }
    }
}

@Composable
private fun CircleColor(
    color: Color,
    selected: Boolean,
    onClick: () -> Unit,
    darkTheme: Boolean
) {
    val borderColor = when {
        !selected -> Color.Transparent
        darkTheme -> Color.White
        else -> Color.Black
    }
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(color)
            .then(
                if (selected) Modifier.border(
                    BorderStroke(3.dp, borderColor),
                    CircleShape
                ) else Modifier
            )
            .clickable { onClick() }
            .padding(4.dp)
    )
}

private val secondaryColorMap = mapOf(
    0xFF2AE881UL to 0xFFD4FAE6UL,
    0xFFD4FAE6UL to 0xFF2AE881UL,
    0xFFE46962UL to 0xFFC4BFC7UL,
    0xFFFCE300UL to 0xFFC4BFC7UL,
    0xFFC4BFC7UL to 0xFF2AE881UL,
    0xFFD0BCFFUL to 0xFF6650A4UL,
    0xFF6650A4UL to 0xFFD0BCFFUL,
    0xFFEFB8C8UL to 0xFF7D5260UL,
    0xFF7D5260UL to 0xFFEFB8C8UL
)

fun autoPickSecondary(primary: ULong): ULong {
    return secondaryColorMap[primary and 0xFFFFFFFFUL] ?: (Grey.value.toULong() and 0xFFFFFFFFUL)
}