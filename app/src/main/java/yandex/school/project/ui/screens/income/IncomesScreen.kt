package yandex.school.project.ui.screens.income

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import yandex.school.project.ui.common.Result
import yandex.school.project.ui.components.ListItem
import yandex.school.project.ui.theme.ProjectTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IncomesScreen(
    viewModel: IncomesViewModel = viewModel(),
    accountId: Int
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(accountId) {
        viewModel.loadTransactions(accountId)
    }

    when (val state = uiState) {
        is Result.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is Result.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.message)
            }
        }

        is Result.Success -> {
            LazyColumn {
                stickyHeader {
                    ListItem(
                        modifier = Modifier.height(56.dp),
                        contentTitle = "Всего",
                        contentSecond = {
                            Text(
                                state.data.total,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        backgroundColor = MaterialTheme.colorScheme.secondary
                    )
                    HorizontalDivider()
                }

                items(state.data.transactions) { transaction ->
                    ListItem(
                        modifier = Modifier.height(70.dp),
                        leadingIcon = transaction.category.emoji,
                        contentTitle = transaction.category.name,
                        comment = if (!transaction.comment.isNullOrEmpty()) {
                            transaction.comment
                        } else {
                            null
                        },
                        contentSecond = {
                            Text(
                                "${transaction.amount} ₽",
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
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun ExpensesScreenPreview() {
    ProjectTheme {
        Surface {
            IncomesScreen(accountId = 1)
        }
    }
}