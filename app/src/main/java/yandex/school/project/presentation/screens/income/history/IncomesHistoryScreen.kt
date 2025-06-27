package yandex.school.project.presentation.screens.income.history

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import yandex.school.project.presentation.components.HistoryScreen
import yandex.school.project.presentation.theme.ProjectTheme

@Composable
fun IncomesHistoryScreen(
    accountId: Int,
    viewModel: IncomesHistoryViewModel = hiltViewModel(),
    onTransactionClick: (Int) -> Unit = {}
) {
    HistoryScreen(
        viewModel = viewModel,
        accountId = accountId,
        onTransactionClick = onTransactionClick
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewIncomesHistoryScreen() {
    ProjectTheme {
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