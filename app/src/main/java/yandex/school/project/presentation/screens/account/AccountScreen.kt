package yandex.school.project.presentation.screens.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
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

@Composable
fun AccountScreen(
    viewModel: AccountViewModel = hiltViewModel(),
    accountId: Int
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(accountId) {
        viewModel.loadAccountWithRetry(accountId)
    }

    ResultScreen(
        result = uiState,
        onRetry = { viewModel.loadAccountWithRetry(accountId) }
    ) { account ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Баланс
            ListItem(
                modifier = Modifier.height(56.dp),
                leadingIcon = "💰",
                contentTitle = "Баланс",
                contentSecond = {
                    Text(
                        text = "${account.balance} ${account.currency}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                backgroundColor = MaterialTheme.colorScheme.secondary,
                iconBackgroundColor = MaterialTheme.colorScheme.surface
            )
            HorizontalDivider()
            // Валюта
            ListItem(
                modifier = Modifier.height(56.dp),
                contentTitle = "Валюта",
                contentSecond = {
                    Text(
                        text = account.currency,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                backgroundColor = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun AccountScreenPreview() {
    ProjectTheme {
        Surface {
            AccountScreen(accountId = 1)
        }
    }
}