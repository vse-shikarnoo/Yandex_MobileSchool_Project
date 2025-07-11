package yandex.school.project.income.history

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import yandex.school.project.income.di.LocalIncomesViewModelFactory

@Composable
fun IncomesHistoryScreen(
    accountId: Int,
    currency: String = yandex.school.project.core.utils.CURRENCY_RUB,
    onTransactionClick: (Int) -> Unit = {}
) {
    val factory = LocalIncomesViewModelFactory.current
    val viewModel: IncomesHistoryViewModel = viewModel(factory = factory)
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
fun PreviewIncomesHistoryScreen() {
    yandex.school.project.core.theme.ProjectTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            IncomesHistoryScreen(accountId = 1) { transactionId ->
                println("Transaction clicked: $transactionId")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IncomesHistoryScreenWithDependencies(
    accountId: Int,
    onTransactionClick: (Int) -> Unit = {}
) {
    IncomesHistoryScreen(
        accountId = accountId,
        onTransactionClick = onTransactionClick
    )
} 