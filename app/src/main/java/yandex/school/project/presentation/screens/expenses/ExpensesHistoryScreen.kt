package yandex.school.project.presentation.screens.expenses

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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import yandex.school.project.presentation.components.ListItem
import yandex.school.project.presentation.theme.ProjectTheme
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpensesHistoryScreen(
    accountId: Int,
    viewModel: ExpensesHistoryViewModel = hiltViewModel(),
    onTransactionClick: (Int) -> Unit = {}
) {
    HistoryScreen(
        viewModel = viewModel,
        accountId = accountId,
        onTransactionClick = onTransactionClick
    )
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
            ExpensesHistoryScreen(accountId = 1) { transactionId ->
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
    ExpensesHistoryScreen(
        accountId = accountId,
        onTransactionClick = onTransactionClick
    )
} 