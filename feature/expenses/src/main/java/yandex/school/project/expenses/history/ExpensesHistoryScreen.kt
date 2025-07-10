package yandex.school.project.expenses.history

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import yandex.school.project.core.ui.components.HistoryScreen
import yandex.school.project.core.theme.ProjectTheme
import yandex.school.project.core.utils.rememberCoroutineManager
import yandex.school.project.core.utils.CURRENCY_RUB
import androidx.lifecycle.viewmodel.compose.viewModel
import yandex.school.project.expenses.di.LocalExpensesViewModelFactory

@Composable
fun ExpensesHistoryScreen(
    modifier: Modifier = Modifier,
    accountId: Int,
    currency: String = CURRENCY_RUB,
    onTransactionClick: (Int) -> Unit = {}
) {
    val factory = LocalExpensesViewModelFactory.current
    val viewModel: ExpensesHistoryViewModel = viewModel(factory = factory)
    val coroutineManager = rememberCoroutineManager(viewModel)

    HistoryScreen(
        accountId = accountId,
        currency = currency,
        onTransactionClick = onTransactionClick,
        coroutineManager = coroutineManager,
        viewModel = viewModel
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewExpensesHistoryScreen() {
    ProjectTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ExpensesHistoryScreen(accountId = 1) { transactionId ->
                println("Transaction clicked: $transactionId")
            }
        }
    }
}

@Composable
fun ExpensesHistoryScreenWithDependencies(
    accountId: Int,
    onTransactionClick: (Int) -> Unit = {}
) {
    ExpensesHistoryScreen(
        accountId = accountId,
        onTransactionClick = onTransactionClick
    )
} 