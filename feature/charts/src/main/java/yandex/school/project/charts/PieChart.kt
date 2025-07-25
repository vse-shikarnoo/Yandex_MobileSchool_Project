package yandex.school.project.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

// Данные для сектора
data class PieChartData(val value: Float, val label: String)

@Composable
fun PieChart(
    data: List<PieChartData>,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    // Агрегация: 5 крупнейших + 'Другое'
    val sorted = data.sortedByDescending { it.value }
    val (main, rest) = if (data.size > 5) sorted.take(5) to sorted.drop(5) else sorted to emptyList()
    val otherValue = rest.sumOf { it.value.toDouble() }.toFloat()
    val chartData = if (rest.isNotEmpty()) main + PieChartData(otherValue, "Другое") else main
    val chartColors = listOf(
        Color(0xFF00E676), // зелёный
        Color(0xFFFFEB3B), // жёлтый
        Color(0xFFFF6D00), // оранжевый
        Color(0xFF2979FF), // синий
        Color(0xFFD500F9), // фиолетовый
        Color(0xFFFF1744)  // красный
    )
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val diameter = minOf(size.width, size.height)
                val strokeWidth = diameter * 0.08f // Толщина кольца
                val radius = diameter / 2 - strokeWidth / 2
                val center = Offset(size.width / 2, size.height / 2)
                var startAngle = -90f
                val sum = chartData.sumOf { it.value.toDouble() }.toFloat()
                chartData.forEachIndexed { i, item ->
                    val sweep = if (sum == 0f) 0f else 360f * (item.value / sum)
                    drawArc(
                        color = chartColors.getOrElse(i) { colorScheme.primary },
                        startAngle = startAngle,
                        sweepAngle = sweep,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
                    )
                    startAngle += sweep
                }
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        Column(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            chartData.forEachIndexed { i, item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Canvas(modifier = Modifier.size(16.dp)) {
                        drawCircle(color = chartColors.getOrElse(i) { colorScheme.primary })
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    androidx.compose.material3.Text(
                        text = item.label,
                        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }
}

@Preview
@Composable
fun PieChartPreview() {
    val data = listOf(
        PieChartData(20f, "Ремонт квартиры"),
        PieChartData(80f, "На собачку")
    )
    val colors = listOf(
        Color(0xFF00E676), // зелёный
        Color(0xFFFFEB3B)  // жёлтый
    )
    Surface {
        Box(Modifier.fillMaxSize()) {
            PieChart(data = data, colors = colors, modifier = Modifier.fillMaxSize())
        }
    }
} 