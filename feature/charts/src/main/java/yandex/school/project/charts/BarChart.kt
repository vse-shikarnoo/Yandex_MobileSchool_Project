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
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val max = data.maxOfOrNull { it.value } ?: 1f
        val min = data.minOfOrNull { it.value } ?: 0f
        val barWidth = size.width / (data.size * 1.5f)
        val barSpace = barWidth / 2
        val zeroY = size.height * (if (min < 0) max / (max - min) else 1f)
        data.forEachIndexed { i, item ->
            val left = i * (barWidth + barSpace) + barSpace / 2
            val valueY = size.height - ((item.value - min) / (max - min).coerceAtLeast(1f)) * size.height
            val color = if (item.value >= 0) Color(0xFF00E676) else Color(0xFFFF6D00)
            drawRoundRect(
                color = color,
                topLeft = Offset(left, valueY),
                size = androidx.compose.ui.geometry.Size(barWidth, size.height - valueY),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(barWidth / 2, barWidth / 2)
            )
        }
    }
}

@Preview
@Composable
fun BarChartPreview() {
    val data = listOf(
        BarChartData(40f, ""),
        BarChartData(30f, ""),
        BarChartData(-10f, ""),
        BarChartData(-20f, ""),
        BarChartData(10f, ""),
        BarChartData(5f, "")
    )
    Surface {
        Box(Modifier.fillMaxSize()) {
            BarChart(data = data, modifier = Modifier.fillMaxSize())
        }
    }
} 