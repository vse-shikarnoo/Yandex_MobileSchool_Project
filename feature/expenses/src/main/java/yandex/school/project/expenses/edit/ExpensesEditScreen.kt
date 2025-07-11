package yandex.school.project.expenses.edit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import yandex.school.project.core.domain.entities.Transaction
import yandex.school.project.core.domain.entities.TransactionType
import yandex.school.project.core.theme.ProjectTheme
import yandex.school.project.core.ui.components.ListItem
import yandex.school.project.core.ui.components.ResultScreen
import yandex.school.project.core.utils.rememberCoroutineManager
import yandex.school.project.expenses.di.LocalExpensesViewModelFactory

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpensesEditScreen(
    modifier: Modifier = Modifier,
    isEditMode: Boolean,
    accountId: Int = 1,
    transactionId: Int? = null,
    onSuccess: () -> Unit
) {
    val factory = LocalExpensesViewModelFactory.current
    val viewModel: ExpensesEditViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()
    val coroutineManager = rememberCoroutineManager(viewModel)

    LaunchedEffect(isEditMode, transactionId) {
        if (isEditMode && transactionId != null) {
            coroutineManager.launchWithCancelPrevious {
                viewModel.loadTransaction(transactionId)
            }
        }
    }

    ResultScreen(
        modifier = modifier,
        result = uiState,
        onRetry = { transactionId?.let { viewModel.loadTransaction(transactionId) } },
        coroutineManager = coroutineManager
    ) { state ->

        var account by remember { mutableIntStateOf(state.transaction?.accountId ?: accountId) }
        var category by remember { mutableIntStateOf(state.transaction?.categoryId ?: 1) }
        var amount by remember { mutableDoubleStateOf(state.transaction?.amount ?: 0.0) }
        var date by remember { mutableStateOf(state.transaction?.date ?: "Test") }
        var time by remember { mutableStateOf(state.transaction?.date ?: "Test") }
        var comment by remember { mutableStateOf(state.transaction?.description ?: "Test") }


        Column {
            ListItem(
                contentTitle = "Счёт",
                comment = account.toString(),
                onClick = { /* выбор счёта */ }
            )
            ListItem(
                contentTitle = "Статья",
                comment = category.toString(),
                onClick = { /* выбор категории */ }
            )
            ListItem(
                contentTitle = "Сумма",
                comment = amount.toString(),
                onClick = { /* ввод суммы */ }
            )
            ListItem(
                contentTitle = "Дата",
                comment = date,
                onClick = { /* выбор даты */ }
            )
            ListItem(
                contentTitle = "Время",
                comment = time,
                onClick = { /* выбор времени */ }
            )
            ListItem(
                contentTitle = "Комментарий",
                comment = comment,
                onClick = { /* ввод комментария */ }
            )

            Spacer(Modifier.height(16.dp))

            if (isEditMode) {
                Button(
                    onClick = {
                        transactionId.let { viewModel.deleteTransaction(it!!) }
                        onSuccess()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Удалить расход")
                }
            }

            Button(
                onClick = {
                    val transaction = Transaction(
                        id = state.transaction?.id ?: -1,
                        accountId = account,
                        categoryId = category,
                        amount = amount,
                        description = comment,
                        date = "2025-07-11T13:41:19.672Z",
                        type = TransactionType.EXPENSE,
                        createdAt = state.transaction?.createdAt ?: "2025-07-11T13:41:19.672Z",
                        updatedAt = state.transaction?.updatedAt ?: "2025-07-11T13:41:19.672Z"
                    )
                    viewModel.saveTransaction(transaction, isEditMode)
                    onSuccess()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(if (isEditMode) "Сохранить" else "Создать")
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun ExpensesScreenPreview() {
    ProjectTheme {
        Surface {
            ExpensesEditScreen(transactionId = 1, isEditMode = false) {}
        }
    }
}