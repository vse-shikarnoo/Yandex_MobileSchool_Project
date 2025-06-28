package yandex.school.project.presentation.screens.expenses.history

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import yandex.school.project.presentation.components.HistoryScreen
import yandex.school.project.presentation.theme.ProjectTheme
import yandex.school.project.presentation.common.rememberCoroutineManager

@Composable
fun ExpensesHistoryScreen(
    accountId: Int,
    viewModel: ExpensesHistoryViewModel = hiltViewModel(),
    onTransactionClick: (Int) -> Unit = {}
) {
    val coroutineManager = rememberCoroutineManager(viewModel)
    
    HistoryScreen(
        viewModel = viewModel,
        accountId = accountId,
        onTransactionClick = onTransactionClick,
        coroutineManager = coroutineManager
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