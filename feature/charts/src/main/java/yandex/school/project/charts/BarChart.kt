package yandex.school.project.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

// Данные для столбца
data class BarChartData(val value: Float, val label: String)

@Composable
fun BarChart(
    data: List<BarChartData>,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    val max = data.maxOfOrNull { it.value } ?: 1f
    val colorScheme = MaterialTheme.colorScheme
    Canvas(modifier = modifier) {
        val barWidth = size.width / (data.size * 2)
        data.forEachIndexed { i, item ->
            val left = i * 2 * barWidth + barWidth / 2
            val top = size.height - (item.value / max) * size.height
            drawRect(
                color = colors.getOrElse(i) { colorScheme.primary },
                topLeft = Offset(left, top),
                size = androidx.compose.ui.geometry.Size(barWidth, size.height - top)
            )
        }
    }
}

@Preview
@Composable
fun BarChartPreview() {
    val data = listOf(
        BarChartData(40f, "A"),
        BarChartData(30f, "B"),
        BarChartData(20f, "C"),
        BarChartData(10f, "D")
    )
    val colors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.error
    )
    Surface {
        Box(Modifier.fillMaxSize()) {
            BarChart(data = data, colors = colors, modifier = Modifier.fillMaxSize())
        }
    }
} 