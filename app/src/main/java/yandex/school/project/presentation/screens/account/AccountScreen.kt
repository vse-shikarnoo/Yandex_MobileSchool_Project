package yandex.school.project.presentation.screens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import yandex.school.project.R
import yandex.school.project.presentation.common.rememberCoroutineManager
import yandex.school.project.presentation.components.ListItem
import yandex.school.project.presentation.components.ResultScreen
import yandex.school.project.presentation.theme.ProjectTheme
import yandex.school.project.presentation.utils.CURRENCY_EUR
import yandex.school.project.presentation.utils.CURRENCY_RUB
import yandex.school.project.presentation.utils.CURRENCY_USD

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
                    BottomSheetItem(
                        icon = {
                            Icon(
                                painterResource(R.drawable.currency_ruble),
                                contentDescription = null
                            )
                        },
                        text = "Российский рубль",
                        onClick = {
                            if (account.currency != CURRENCY_RUB) onCurrencyChanged(CURRENCY_RUB)
                            viewModel.updateAccountCurrency(CURRENCY_RUB)
                            showCurrencySheet = false
                        }
                    )
                    HorizontalDivider()
                    BottomSheetItem(
                        icon = {
                            Icon(
                                painterResource(R.drawable.attach_money),
                                contentDescription = null
                            )
                        },
                        text = "Американский доллар",
                        onClick = {
                            if (account.currency != CURRENCY_USD) onCurrencyChanged(CURRENCY_USD)
                            viewModel.updateAccountCurrency(CURRENCY_USD)
                            showCurrencySheet = false
                        }
                    )
                    HorizontalDivider()
                    BottomSheetItem(
                        icon = {
                            Icon(
                                painterResource(R.drawable.euro),
                                contentDescription = null
                            )
                        },
                        text = "Евро",
                        onClick = {
                            if (account.currency != CURRENCY_EUR) onCurrencyChanged(CURRENCY_EUR)
                            viewModel.updateAccountCurrency(CURRENCY_EUR)
                            showCurrencySheet = false
                        }
                    )
                    HorizontalDivider()
                    BottomSheetItem(
                        icon = {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = null,
                                tint = Color.White
                            )
                        },
                        text = "Отмена",
                        textColor = Color.White,
                        backgroundColor = MaterialTheme.colorScheme.error,
                        onClick = { showCurrencySheet = false }
                    )
                }
            }
        }
    }
}

//TODO: надо перебить в ListItem
@Composable
fun BottomSheetItem(
    icon: @Composable (() -> Unit)? = null,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    backgroundColor: Color = Color.Transparent,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable { onClick() }
            .height(56.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.width(16.dp))
            }
            Text(text, color = textColor, style = MaterialTheme.typography.bodyLarge)
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