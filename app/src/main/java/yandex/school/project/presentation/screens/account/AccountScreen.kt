package yandex.school.project.presentation.screens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import yandex.school.project.presentation.common.rememberCoroutineManager
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Button
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.ListItem
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import yandex.school.project.R
import yandex.school.project.presentation.utils.CURRENCY_RUB
import yandex.school.project.presentation.utils.CURRENCY_USD
import yandex.school.project.presentation.utils.CURRENCY_EUR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    accountId: Int,
    currency: String,
    onCurrencyChanged: (String) -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineManager = rememberCoroutineManager(viewModel)
    var showCurrencySheet by remember { mutableStateOf(false) }

    LaunchedEffect(accountId) {
        coroutineManager.launchWithCancelPrevious {
            viewModel.loadAccountWithRetry(accountId)
        }
    }

    ResultScreen(
        result = uiState,
        onRetry = { viewModel.loadAccountWithRetry(accountId) },
        coroutineManager = coroutineManager
    ) { account ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Баланс
            ListItem(
                modifier = Modifier.height(56.dp),
                contentTitle = "Баланс",
                contentSecond = {
                    Text(
                        text = "${account.balance} $currency",
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
                        text = currency,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                backgroundColor = MaterialTheme.colorScheme.secondary,
                onClick = { showCurrencySheet = true }
            )
        }
        if (showCurrencySheet) {
            ModalBottomSheet(onDismissRequest = { showCurrencySheet = false }) {
                Column {
                    ListItem(
                        leadingContent = { Icon(painter = painterResource(R.drawable.currency_ruble), contentDescription = null) },
                        headlineContent = { Text("Российский рубль") },
                        modifier = Modifier.clickable {
                            if (account.currency != CURRENCY_RUB) {
                                onCurrencyChanged(CURRENCY_RUB)
                            }
                            showCurrencySheet = false
                        }
                    )
                    Divider()
                    ListItem(
                        leadingContent = { Icon(painter = painterResource(R.drawable.attach_money), contentDescription = null) },
                        headlineContent = { Text("Американский доллар") },
                        modifier = Modifier.clickable {
                            if (account.currency != CURRENCY_USD) {
                                onCurrencyChanged(CURRENCY_USD)
                            }
                            showCurrencySheet = false
                        }
                    )
                    Divider()
                    ListItem(
                        leadingContent = { Icon(painter = painterResource(R.drawable.euro), contentDescription = null) },
                        headlineContent = { Text("Евро") },
                        modifier = Modifier.clickable {
                            if (account.currency != CURRENCY_EUR) {
                                onCurrencyChanged(CURRENCY_EUR)
                            }
                            showCurrencySheet = false
                        }
                    )
                    Divider()
                    ListItem(
                        leadingContent = { Icon(Icons.Filled.Close, contentDescription = null, tint = Color.White) },
                        headlineContent = { Text("Отмена", color = Color.White) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.error)
                            .clickable { showCurrencySheet = false }
                    )
                }
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun AccountScreenPreview() {
    ProjectTheme {
        Surface {
            AccountScreen(accountId = 1, currency = "₽", onCurrencyChanged = {})
        }
    }
}