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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview

// Данные для точки на линии
data class LineChartData(val value: Float, val label: String)

@Composable
fun LineChart(
    data: List<LineChartData>,
    color: Color,
    modifier: Modifier = Modifier
) {
    val max = data.maxOfOrNull { it.value } ?: 1f
    val min = data.minOfOrNull { it.value } ?: 0f
    Canvas(modifier = modifier) {
        if (data.size < 2) return@Canvas
        val stepX = size.width / (data.size - 1)
        val range = (max - min).takeIf { it > 0 } ?: 1f
        val points = data.mapIndexed { i, item ->
            Offset(
                x = i * stepX,
                y = size.height - ((item.value - min) / range) * size.height
            )
        }
        for (i in 0 until points.size - 1) {
            drawLine(
                color = color,
                start = points[i],
                end = points[i + 1],
                strokeWidth = 4f
            )
        }
    }
}

@Preview
@Composable
fun LineChartPreview() {
    val data = listOf(
        LineChartData(10f, "A"),
        LineChartData(30f, "B"),
        LineChartData(20f, "C"),
        LineChartData(40f, "D")
    )
    Surface {
        Box(Modifier.fillMaxSize()) {
            LineChart(data = data, color = MaterialTheme.colorScheme.primary, modifier = Modifier.fillMaxSize())
        }
    }
} 