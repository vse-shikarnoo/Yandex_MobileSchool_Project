package yandex.school.project.core.ui.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import yandex.school.project.core.utils.CURRENCY_RUB

/**
 * Универсальный экран истории транзакций (расходы/доходы).
 * Отвечает только за отображение истории и выбор дат.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen(
    viewModel: yandex.school.project.core.ui.common.HistoryViewModel,
    accountId: Int,
    currency: String,
    onTransactionClick: (Int) -> Unit = {},
    coroutineManager: yandex.school.project.core.utils.CoroutineManager? = null
) {
    val uiState = viewModel.uiState

    LaunchedEffect(accountId) {
        viewModel.loadTransactionsWithRetry(accountId)
    }

    ResultScreen(
        result = uiState,
        onRetry = { viewModel.loadTransactionsWithRetry(accountId) },
        coroutineManager = coroutineManager
    ) { data ->
        val transactions = data.transactions
        val startDate = data.startDate
        val endDate = data.endDate
        val totalAmount = data.totalAmount
        val errorMessage = data.errorMessage

        var showStartDatePicker by remember { mutableStateOf(false) }
        var showEndDatePicker by remember { mutableStateOf(false) }

        DatePickerDialogComponent(
            show = showStartDatePicker,
            initialDate = startDate,
            onDateSelected = { newDate -> 
                if (coroutineManager != null) {
                    coroutineManager.launchWithCancelPrevious {
                        viewModel.onDateRangeSelected(accountId, newDate, endDate ?: newDate)
                    }
                } else {
                    viewModel.onDateRangeSelected(accountId, newDate, endDate ?: newDate)
                }
            },
            onDismiss = { showStartDatePicker = false }
        )
        DatePickerDialogComponent(
            show = showEndDatePicker,
            initialDate = endDate,
            onDateSelected = { newDate -> 
                if (coroutineManager != null) {
                    coroutineManager.launchWithCancelPrevious {
                        viewModel.onDateRangeSelected(accountId, startDate ?: newDate, newDate)
                    }
                } else {
                    viewModel.onDateRangeSelected(accountId, startDate ?: newDate, newDate)
                }
            },
            onDismiss = { showEndDatePicker = false }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            stickyHeader {
                ListItem(
                    modifier = Modifier.height(56.dp),
                    contentTitle = "Начало",
                    contentSecond = {
                        Text(startDate?.let { DateTimeFormatter.ofPattern("dd-MM-yyyy").format(it) }
                            ?: "-")
                    },
                    onClick = { showStartDatePicker = true },
                    backgroundColor = MaterialTheme.colorScheme.secondary
                )
                HorizontalDivider()
                ListItem(
                    modifier = Modifier.height(56.dp),
                    contentTitle = "Конец",
                    contentSecond = {
                        Text(endDate?.let { DateTimeFormatter.ofPattern("dd-MM-yyyy").format(it) }
                            ?: "-")
                    },
                    onClick = { showEndDatePicker = true },
                    backgroundColor = MaterialTheme.colorScheme.secondary
                )
                HorizontalDivider()
                ListItem(
                    modifier = Modifier.height(56.dp),
                    contentTitle = "Сумма",
                    contentSecond = {
                        Text("${totalAmount.toInt()} $currency")
                    },
                    backgroundColor = MaterialTheme.colorScheme.secondary
                )
                HorizontalDivider()
            }
            items (transactions) { transactionWithCategory ->
                ListItem(
                    leadingIcon = transactionWithCategory.categoryIcon ?: "📁",
                    contentTitle = transactionWithCategory.categoryName,
                    comment = if (!transactionWithCategory.description.isNullOrEmpty()) {
                        transactionWithCategory.description
                    } else {
                        null
                    },
                    contentSecond = {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${transactionWithCategory.amount} $CURRENCY_RUB",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            val formattedDate = try {
                                val dt = OffsetDateTime.parse(transactionWithCategory.date)
                                dt.format(DateTimeFormatter.ofPattern("dd-MM-yy HH:mm"))
                            } catch (e: DateTimeParseException) {
                                Log.e("HistoryScreen", "transactionDate parse", e)
                                transactionWithCategory.date
                            }
                            Text(
                                text = formattedDate,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    },
                    trailing = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    },
                    onClick = { onTransactionClick(transactionWithCategory.id) },
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.height(56.dp)
                )
                HorizontalDivider()
            }
        }
    }
} 