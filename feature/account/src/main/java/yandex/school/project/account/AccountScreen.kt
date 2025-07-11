package yandex.school.project.account

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import yandex.school.project.account.di.AccountComponent
import yandex.school.project.account.di.LocalAccountViewModelFactory

@Composable
fun ProvidedAccountScreen(
    accountComponent: AccountComponent,
    accountId: Int,
    currency: String,
    onCurrencyChanged: (String) -> Unit
){
    val viewModelFactory = remember { accountComponent.viewModelFactory() }
    CompositionLocalProvider(LocalAccountViewModelFactory provides viewModelFactory) {
        AccountScreen (
            accountId,
            currency,
            onCurrencyChanged
        )
    }
}

@Composable
internal fun AccountScreen(
    accountId: Int,
    currency: String,
    onCurrencyChanged: (String) -> Unit
) {
    val factory = LocalAccountViewModelFactory.current
    val viewModel: AccountViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()
    val coroutineManager = yandex.school.project.core.utils.rememberCoroutineManager(viewModel)
    var showCurrencySheet by remember { mutableStateOf(false) }

    var isEditingBalance by remember { mutableStateOf(false) }
    var balanceInput by remember { mutableStateOf("") }

    Log.d("AccountScreen", "RECOMPOSE: account.balance=${uiState.let { (it as? yandex.school.project.core.utils.Result.Success)?.data?.balance }}, isEditingBalance=$isEditingBalance, balanceInput=$balanceInput")

    LaunchedEffect(accountId) {
        coroutineManager.launchWithCancelPrevious {
            viewModel.loadAccountWithRetry(accountId)
        }
    }

    yandex.school.project.core.ui.components.ResultScreen(
        result = uiState,
        onRetry = { viewModel.loadAccountWithRetry(accountId) },
        coroutineManager = coroutineManager
    ) { account ->

        LaunchedEffect(account.balance, isEditingBalance) {
            if (!isEditingBalance) {
                balanceInput = account.balance.toString()
            }
        }
        val focusManager = LocalFocusManager.current
        val focusRequester = remember { FocusRequester() }
        val keyboardController = LocalSoftwareKeyboardController.current
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    if (isEditingBalance) {
                        focusManager.clearFocus()
                        isEditingBalance = false
                    }
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Баланс
                yandex.school.project.core.ui.components.ListItem(
                    modifier = Modifier
                        .height(56.dp)
                        .clickable {
                            if (!isEditingBalance) {
                                isEditingBalance = true
                                balanceInput = account.balance.toString()
                                Log.d(
                                    "AccountScreen",
                                    "START EDIT: balanceInput=$balanceInput, account.balance=${account.balance}"
                                )
                            }
                        },
                    contentTitle = "Баланс",
                    contentSecond = {
                        if (isEditingBalance) {
                            EditableBalanceField(
                                isEditing = true,
                                balanceInput = balanceInput,
                                currency = currency,
                                focusRequester = focusRequester,
                                focusManager = focusManager,
                                keyboardController = keyboardController,
                                onValueChange = { input ->
                                    balanceInput = input.replace(",", ".")
                                    Log.d("AccountScreen", "INPUT: balanceInput=$balanceInput")
                                },
                                onDone = {
                                    var normalized =
                                        balanceInput.replaceFirst("^0+(?!$|\\.)".toRegex(), "")
                                    normalized = when {
                                        normalized.isEmpty() -> "0.00"
                                        normalized.endsWith(".") -> normalized + "00"
                                        normalized.contains(".") -> {
                                            val parts = normalized.split(".")
                                            val afterDot = parts[1].take(2).padEnd(2, '0')
                                            "${parts[0]}.$afterDot"
                                        }

                                        else -> normalized + ".00"
                                    }
                                    balanceInput = normalized
                                    isEditingBalance = false
                                    focusManager.clearFocus()
                                    Log.d(
                                        "AccountScreen",
                                        "ON_DONE: balanceInput=$balanceInput, account.balance=${account.balance}"
                                    )
                                    viewModel.updateAccountBalance(balanceInput.toDouble())
                                }
                            )
                        } else {
                            Text(
                                text = "${account.balance} $currency",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End
                            )
                        }
                    },
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    iconBackgroundColor = MaterialTheme.colorScheme.surface
                )
                HorizontalDivider()
                // Валюта
                CurrencyListItem(
                    currency = currency,
                    onClick = { showCurrencySheet = true }
                )
            }
        }
        if (showCurrencySheet) {
            CurrencyBottomSheet(
                account = account,
                onCurrencySelected = { newCurrency ->
                    if (newCurrency != currency) {
                        onCurrencyChanged(newCurrency)
                        viewModel.updateAccountCurrency(newCurrency)
                    }
                    showCurrencySheet = false
                },
                onDismiss = { showCurrencySheet = false }
            )
        }
    }
}

@Composable
private fun EditableBalanceField(
    isEditing: Boolean,
    balanceInput: String,
    currency: String,
    focusRequester: FocusRequester,
    focusManager: androidx.compose.ui.focus.FocusManager,
    keyboardController: androidx.compose.ui.platform.SoftwareKeyboardController?,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit
) {
    if (isEditing) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = balanceInput,
                onValueChange = onValueChange,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onDone() }
                ),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.End
                ),
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = currency,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    } else {
        Text(
            text = "$balanceInput $currency",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun CurrencyListItem(
    currency: String,
    onClick: () -> Unit
) {
    yandex.school.project.core.ui.components.ListItem(
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
        onClick = onClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencyBottomSheet(
    account: yandex.school.project.core.domain.entities.Account,
    onCurrencySelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column {
            BottomSheetItem(
                icon = { Icon(painterResource(R.drawable.currency_ruble), contentDescription = null) },
                text = "Российский рубль",
                onClick = { onCurrencySelected(yandex.school.project.core.utils.CURRENCY_RUB) }
            )
            HorizontalDivider()
            BottomSheetItem(
                icon = { Icon(painterResource(R.drawable.attach_money), contentDescription = null) },
                text = "Американский доллар",
                onClick = { onCurrencySelected(yandex.school.project.core.utils.CURRENCY_USD) }
            )
            HorizontalDivider()
            BottomSheetItem(
                icon = { Icon(painterResource(R.drawable.euro), contentDescription = null) },
                text = "Евро",
                onClick = { onCurrencySelected(yandex.school.project.core.utils.CURRENCY_EUR) }
            )
            HorizontalDivider()
            BottomSheetItem(
                icon = { Icon(Icons.Filled.Close, contentDescription = null, tint = Color.White) },
                text = "Отмена",
                textColor = Color.White,
                backgroundColor = MaterialTheme.colorScheme.error,
                onClick = onDismiss
            )
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
            .height(72.dp)
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
    yandex.school.project.core.theme.ProjectTheme {
        Surface {
            AccountScreen(accountId = 1, currency = "₽", onCurrencyChanged = {})
        }
    }
}