package yandex.school.project.expenses.edit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import yandex.school.project.core.ui.components.CategorySelectorListItem
import yandex.school.project.core.ui.components.CommentTextListItem
import yandex.school.project.core.ui.components.DateSelectorListItem
import yandex.school.project.core.ui.components.EditArgsText
import yandex.school.project.core.ui.components.TimeSelectorListItem
import yandex.school.project.income.di.LocalIncomesViewModelFactory
import yandex.school.project.income.edit.IncomesEditViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IncomesEditScreen(
    modifier: Modifier = Modifier,
    isEditMode: Boolean,
    accountId: Int = 1,
    transactionId: Int? = null,
    onSuccess: () -> Unit
) {
    val factory = LocalIncomesViewModelFactory.current
    val viewModel: IncomesEditViewModel = viewModel(factory = factory)
    val categories by viewModel.categories.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val coroutineManager = rememberCoroutineManager(viewModel)

    LaunchedEffect(isEditMode, transactionId) {
        if (isEditMode && transactionId != null) {
            coroutineManager.launchWithCancelPrevious {
                viewModel.loadTransaction(transactionId)
            }
        }
        viewModel.loadCategories()
    }

    ResultScreen(
        modifier = modifier,
        result = uiState,
        onRetry = { transactionId?.let { viewModel.loadTransaction(transactionId) } },
        coroutineManager = coroutineManager
    ) { state ->

        var account by remember { mutableIntStateOf(accountId) }
        var category by remember { mutableIntStateOf(1) }
        var amount by remember { mutableDoubleStateOf(0.0) }
        var date by remember { mutableStateOf(OffsetDateTime.now(java.time.ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)) }
        var time by remember { mutableStateOf(OffsetDateTime.now(java.time.ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)) }
        var comment by remember { mutableStateOf("Test") }

        LaunchedEffect(state.transaction) {
            state.transaction?.let { transaction ->
                account = transaction.accountId
                category = transaction.categoryId
                amount = transaction.amount
                date = transaction.date
                time = transaction.date
                comment = transaction.description?:""
            }
        }

        Column {
            ListItem(
                contentTitle = "Счёт",
                contentSecond = {
                    Text(account.toString())
                },
                onClick = { /* выбор счёта */ }
            )
            HorizontalDivider()
            CategorySelectorListItem(
                categories = categories,
                selectedCategoryId = category,
                onCategorySelected = { category = it }
            )
            EditArgsText(
                title = "Сумма",
                value = amount.toString(),
                onValueChange = {
                    amount = it.toDouble()
                }
            )
            DateSelectorListItem(
                date = date,
                onDateChange = { date = it; time = it } // чтобы дата и время были синхронизированы
            )
            TimeSelectorListItem(
                date = time,
                onTimeChange = { time = it; date = it } // чтобы дата и время были синхронизированы
            )
            CommentTextListItem(
                comment = comment,
                onCommentChange = { comment = it },
                maxLength = 40
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
                        date = date,
                        type = TransactionType.INCOME,
                        createdAt = state.transaction?.createdAt ?: date,
                        updatedAt = OffsetDateTime.now(java.time.ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
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
fun IncomesEditScreenPreview() {
    ProjectTheme {
        Surface {
            IncomesEditScreen(transactionId = 1, isEditMode = false) {}
        }
    }
}