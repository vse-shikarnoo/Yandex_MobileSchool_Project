package yandex.school.project.ui.screens.account

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import yandex.school.project.ui.common.Result
import yandex.school.project.ui.components.ErrorItem
import yandex.school.project.ui.components.ListItem
import yandex.school.project.ui.theme.ProjectTheme

@Composable
fun AccountScreen(
    viewModel: AccountViewModel = viewModel(),
    accountId: Int
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(accountId) {
        viewModel.loadAccountWithRetry(accountId)
    }

    when (val state = uiState) {
        is Result.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is Result.Error -> {
            ErrorItem(
                errorMessage = state.message
            ) {
                viewModel.loadAccountWithRetry(accountId = accountId)
            }
        }
        is Result.Success -> {
            val account = state.data
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
                            text = account.balance,
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