package yandex.school.project.expenses.analysis

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import yandex.school.project.core.ui.components.DateSelectorListItem
import yandex.school.project.core.ui.components.ListItem
import yandex.school.project.core.ui.components.ResultScreen
import yandex.school.project.core.utils.CURRENCY_RUB
import yandex.school.project.core.utils.rememberCoroutineManager
import yandex.school.project.expenses.di.LocalExpensesViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import yandex.school.project.charts.PieChart
import yandex.school.project.charts.PieChartData
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row

@Composable
fun ExpensesAnalysisScreen(
    modifier: Modifier = Modifier,
    accountId: Int,
    onBack: () -> Unit = {}
) {
    val factory = LocalExpensesViewModelFactory.current
    val viewModel: ExpensesAnalysisViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()
    val coroutineManager = rememberCoroutineManager(viewModel)

    // Инициализация accountId
    androidx.compose.runtime.LaunchedEffect(accountId) {
        viewModel.setAccountId(accountId)
    }

    ResultScreen(
        result = uiState,
        onRetry = { viewModel.setAccountId(accountId) },
        coroutineManager = coroutineManager
    ) { state ->
        Column(modifier = modifier) {
            // Выбор периода
            DateSelectorListItem(
                date = state.startDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                onDateChange = {
                    val newStart = LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE)
                    viewModel.setPeriod(newStart, state.endDate)
                },
                modifier = Modifier.fillMaxWidth()
            )
            DateSelectorListItem(
                date = state.endDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                onDateChange = {
                    val newEnd = LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE)
                    viewModel.setPeriod(state.startDate, newEnd)
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            // Сумма
            ListItem(
                contentTitle = "Сумма",
                contentSecond = {
                    Text(
                        text = "${state.total.toInt()} $CURRENCY_RUB",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            )
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))
            // Добавляем PieChart
            // Оставляю только вызов PieChart без Row и Column для легенды
            PieChart(
                data = state.categories.map { PieChartData(it.percent.toFloat(), it.category.name) },
                colors = List(state.categories.size) { i ->
                    val baseColors = listOf(
                        Color(0xFF00E676), // зелёный
                        Color(0xFFFFEB3B), // жёлтый
                        Color(0xFFFF6D00), // оранжевый
                        Color(0xFF2979FF), // синий
                        Color(0xFFD500F9), // фиолетовый
                        Color(0xFFFF1744), // красный
                        Color(0xFF00B8D4), // бирюзовый
                        Color(0xFF8D6E63)  // коричневый
                    )
                    baseColors[i % baseColors.size]
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 32.dp)
            )
            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            // Список категорий
            LazyColumn {
                items(state.categories) { cat ->
                    ListItem(
                        leadingIcon = cat.category.icon,
                        contentTitle = cat.category.name,
                        comment = cat.transactions.firstOrNull()?.description,
                        contentSecond = {
                            Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                                Text("${cat.percent}%", style = MaterialTheme.typography.bodyLarge)
                                Text("${cat.amount.toInt()} $CURRENCY_RUB", style = MaterialTheme.typography.bodyLarge)
                            }
                        },
                        trailing = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        },
                        modifier = Modifier.fillMaxWidth().height(70.dp),
                        onClick = { /* TODO: переход к деталям */ }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
} 