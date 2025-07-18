package yandex.school.project.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// Данные для сектора
data class PieChartData(val value: Float, val label: String)

@Composable
fun PieChart(
    data: List<PieChartData>,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .aspectRatio(1f) // всегда квадрат
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val diameter = minOf(size.width, size.height)
            val topLeftX = (size.width - diameter) / 2
            val topLeftY = (size.height - diameter) / 2
            var startAngle = -90f
            val sum = data.sumOf { it.value.toDouble() }.toFloat()
            data.forEachIndexed { i, item ->
                val sweep = if (sum == 0f) 0f else 360f * (item.value / sum)
                drawArc(
                    color = colors.getOrElse(i) { colorScheme.primary },
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = true,
                    topLeft = Offset(topLeftX, topLeftY),
                    size = Size(diameter, diameter)
                )
                startAngle += sweep
            }
        }
    }
}

@Preview
@Composable
fun PieChartPreview() {
    val data = listOf(
        PieChartData(40f, "A"),
        PieChartData(30f, "B"),
        PieChartData(20f, "C"),
        PieChartData(10f, "D")
    )
    val colors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.error
    )
    Surface {
        Box(Modifier.fillMaxSize()) {
            PieChart(data = data, colors = colors, modifier = Modifier.fillMaxSize())
        }
    }
} 