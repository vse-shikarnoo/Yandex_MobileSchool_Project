package yandex.school.project.ui.screens.expenses

import android.app.DatePickerDialog
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import yandex.school.project.ui.components.ListItem
import yandex.school.project.ui.theme.ProjectTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpensesHistoryScreen(
    viewModel: ExpensesHistoryViewModel = viewModel(),
    onTransactionClick: (Int) -> Unit = {}
) {
    val transactions = viewModel.transactions
    val startDate = viewModel.startDate
    val endDate = viewModel.endDate
    val totalAmount = viewModel.totalAmount

    val context = LocalContext.current
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    // DatePickerDialog для начала
    if (showStartDatePicker) {
        val calendar = Calendar.getInstance()
        val year = startDate?.year ?: calendar.get(Calendar.YEAR)
        val month = startDate?.monthValue?.minus(1) ?: calendar.get(Calendar.MONTH)
        val day = startDate?.dayOfMonth ?: calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(
            context,
            { _, y, m, d ->
                val newDate = LocalDate.of(y, m + 1, d)
                viewModel.onDateRangeSelected(newDate, endDate ?: newDate)
                showStartDatePicker = false
            },
            year, month, day
        ).apply { setOnDismissListener { showStartDatePicker = false } }.show()
    }
    // DatePickerDialog для конца
    if (showEndDatePicker) {
        val calendar = Calendar.getInstance()
        val year = endDate?.year ?: calendar.get(Calendar.YEAR)
        val month = endDate?.monthValue?.minus(1) ?: calendar.get(Calendar.MONTH)
        val day = endDate?.dayOfMonth ?: calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(
            context,
            { _, y, m, d ->
                val newDate = LocalDate.of(y, m + 1, d)
                viewModel.onDateRangeSelected(startDate ?: newDate, newDate)
                showEndDatePicker = false
            },
            year, month, day
        ).apply { setOnDismissListener { showEndDatePicker = false } }.show()
    }


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
                    Text("${totalAmount.toInt()} ₽")
                },
                backgroundColor = MaterialTheme.colorScheme.secondary
            )
            HorizontalDivider()
        }
        items(transactions) { transaction ->
            ListItem(
                leadingIcon = transaction.category.emoji,
                contentTitle = transaction.category.name,
                comment = transaction.comment,
                contentSecond = {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${transaction.amount} ₽",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        val formattedDate = try {
                            val dt = OffsetDateTime.parse(transaction.transactionDate)
                            dt.format(DateTimeFormatter.ofPattern("dd-MM-yy HH:mm"))
                        } catch (e: DateTimeParseException) {
                            Log.e("ExpensesHistoryScreen", "ExpensesHistoryScreen: transactionDate parse", e)
                            transaction.transactionDate
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
                onClick = { onTransactionClick(transaction.id) },
                backgroundColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.height(56.dp)
            )
            Divider()
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewExpensesHistoryScreen() {
    ProjectTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ExpensesHistoryScreen() { transactionId ->
                println("Transaction clicked: $transactionId")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpensesHistoryScreenWithDependencies(
    accountId: Int,
    onTransactionClick: (Int) -> Unit = {}
) {
    ExpensesHistoryScreen(onTransactionClick = onTransactionClick)
} 