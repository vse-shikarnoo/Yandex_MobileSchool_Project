package yandex.school.project.ui.screens.expenses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import yandex.school.project.data.models.AccountBrief
import yandex.school.project.data.models.Category
import yandex.school.project.data.models.TransactionResponse
import yandex.school.project.ui.components.ListItem
import yandex.school.project.ui.theme.ProjectTheme

private val sampleTransactions = listOf(
    TransactionResponse(
        id = 1,
        account = AccountBrief(1, "Основной счёт", "1000.00", "RUB"),
        category = Category(1, "Еда", "🍔", false),
        amount = "500.00",
        transactionDate = "2024-03-20T10:00:00Z",
        comment = "Обед в кафе",
        createdAt = "2024-03-20T10:00:00Z",
        updatedAt = "2024-03-20T10:00:00Z"
    ),
    TransactionResponse(
        id = 2,
        account = AccountBrief(1, "Основной счёт", "500.00", "RUB"),
        category = Category(2, "Транспорт", "🚌", false),
        amount = "150.00",
        transactionDate = "2024-03-19T15:30:00Z",
        comment = "Поездка на автобусе",
        createdAt = "2024-03-19T15:30:00Z",
        updatedAt = "2024-03-19T15:30:00Z"
    ),
    TransactionResponse(
        id = 3,
        account = AccountBrief(2, "Сбережения", "2000.00", "USD"),
        category = Category(3, "Развлечения", "🎬", false),
        amount = "25.00",
        transactionDate = "2024-03-18T20:00:00Z",
        comment = "Билет в кино",
        createdAt = "2024-03-18T20:00:00Z",
        updatedAt = "2024-03-18T20:00:00Z"
    )
)

@Composable
fun ExpensesHistoryScreen(
    transactions: List<TransactionResponse> = sampleTransactions,
    onTransactionClick: (Int) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(transactions) { transaction ->
                ListItem(
                    leadingIcon = transaction.category.emoji,
                    contentTitle = transaction.category.name,
                    contentSecond = {
                        Text(
                            text = "${transaction.amount} ${transaction.account.currency}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    trailing = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null
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
}

@Preview(showBackground = true)
@Composable
fun PreviewExpensesHistoryScreen() {
    ProjectTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ExpensesHistoryScreen(sampleTransactions) { transactionId ->
                println("Transaction clicked: $transactionId")
            }
        }
    }
} 