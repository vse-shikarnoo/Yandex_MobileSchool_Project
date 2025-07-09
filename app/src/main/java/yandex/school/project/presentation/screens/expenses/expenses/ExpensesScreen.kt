package yandex.school.project.presentation.screens.expenses.expenses

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import yandex.school.project.core.utils.rememberCoroutineManager
import yandex.school.project.core.ui.components.ListItem
import yandex.school.project.core.ui.components.ResultScreen
import yandex.school.project.core.theme.ProjectTheme
import yandex.school.project.core.utils.convertAmount
import yandex.school.project.core.utils.CURRENCY_RUB
import androidx.lifecycle.viewmodel.compose.viewModel
import yandex.school.project.LocalViewModelFactory

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpensesScreen(
    accountId: Int,
    currency: String = yandex.school.project.core.utils.CURRENCY_RUB
) {
    val factory = LocalViewModelFactory.current
    val viewModel: ExpensesViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()
    val coroutineManager = yandex.school.project.core.utils.rememberCoroutineManager(viewModel)

    Log.d("ExpensesScreen", "ExpensesScreen: $currency")
    
    LaunchedEffect(accountId) {
        coroutineManager.launchWithCancelPrevious {
            viewModel.loadTransactionsWithRetry(accountId)
        }
    }

    yandex.school.project.core.ui.components.ResultScreen(
        result = uiState,
        onRetry = { viewModel.loadTransactionsWithRetry(accountId) },
        coroutineManager = coroutineManager
    ) { state ->
        LazyColumn {
            stickyHeader {
                yandex.school.project.core.ui.components.ListItem(
                    modifier = Modifier.height(56.dp),
                    contentTitle = "Всего",
                    contentSecond = {
                        val total = yandex.school.project.core.utils.convertAmount(
                            state.total.toDoubleOrNull() ?: 0.0,
                            yandex.school.project.core.utils.CURRENCY_RUB,
                            currency
                        )
                        Text(
                            "${total.toInt()} $currency",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    backgroundColor = MaterialTheme.colorScheme.secondary
                )
                HorizontalDivider()
            }

            items(state.transactions) { transactionWithCategory ->
                yandex.school.project.core.ui.components.ListItem(
                    modifier = Modifier.height(70.dp),
                    leadingIcon = transactionWithCategory.categoryIcon ?: "\uD83D\uDCC1",
                    contentTitle = transactionWithCategory.categoryName,
                    comment = if (!transactionWithCategory.description.isNullOrEmpty()) {
                        transactionWithCategory.description
                    } else {
                        null
                    },
                    contentSecond = {
                        val amount = yandex.school.project.core.utils.convertAmount(
                            transactionWithCategory.amount,
                            yandex.school.project.core.utils.CURRENCY_RUB,
                            currency
                        )
                        Text(
                            "${amount.toInt()} $currency",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    trailing = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    },
                    onClick = { /* TODO: переход к деталям */ }
                )
                HorizontalDivider()
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun ExpensesScreenPreview() {
    yandex.school.project.core.theme.ProjectTheme {
        Surface {
            ExpensesScreen(accountId = 1)
        }
    }
}