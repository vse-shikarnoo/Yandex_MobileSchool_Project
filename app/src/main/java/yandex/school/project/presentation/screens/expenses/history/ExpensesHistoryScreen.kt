package yandex.school.project.presentation.screens.expenses.history

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
import yandex.school.project.LocalViewModelFactory

@Composable
fun ExpensesHistoryScreen(
    accountId: Int,
    currency: String = yandex.school.project.core.utils.CURRENCY_RUB,
    onTransactionClick: (Int) -> Unit = {}
) {
    val factory = LocalViewModelFactory.current
    val viewModel: ExpensesHistoryViewModel = viewModel(factory = factory)
    val coroutineManager = yandex.school.project.core.utils.rememberCoroutineManager(viewModel)

    yandex.school.project.core.ui.components.HistoryScreen(
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
    yandex.school.project.core.theme.ProjectTheme {
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