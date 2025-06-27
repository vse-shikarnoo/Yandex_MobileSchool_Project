package yandex.school.project.presentation.screens.expenses.expenses

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.hilt.navigation.compose.hiltViewModel
import yandex.school.project.presentation.components.ListItem
import yandex.school.project.presentation.components.ResultScreen
import yandex.school.project.presentation.theme.ProjectTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpensesScreen(
    viewModel: ExpensesViewModel = hiltViewModel(),
    accountId: Int
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(accountId) {
        viewModel.loadTransactionsWithRetry(accountId)
    }

    ResultScreen(
        result = uiState,
        onRetry = { viewModel.loadTransactionsWithRetry(accountId) }
    ) { state ->
        LazyColumn {
            stickyHeader {
                ListItem(
                    modifier = Modifier.height(56.dp),
                    contentTitle = "Всего",
                    contentSecond = {
                        Text(
                            state.total,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    backgroundColor = MaterialTheme.colorScheme.secondary
                )
                HorizontalDivider()
            }

            items(state.transactions) { transactionWithCategory ->
                ListItem(
                    modifier = Modifier.height(70.dp),
                    leadingIcon = transactionWithCategory.categoryIcon ?: "📁",
                    contentTitle = transactionWithCategory.categoryName,
                    comment = if (!transactionWithCategory.description.isNullOrEmpty()) {
                        transactionWithCategory.description
                    } else {
                        null
                    },
                    contentSecond = {
                        Text(
                            "${transactionWithCategory.amount} ₽",
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
    ProjectTheme {
        Surface {
            ExpensesScreen(accountId = 1)
        }
    }
}