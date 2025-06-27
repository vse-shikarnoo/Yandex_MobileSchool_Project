package yandex.school.project.presentation.screens.expenses.create

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import yandex.school.project.presentation.theme.ProjectTheme

@Composable
fun ExpensesCreateScreen() {
    // TODO: Реализовать экран создания расходов
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "ExpensesCreate")
    }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun ExpensesCreateScreenPreview() {
    ProjectTheme {
        ExpensesCreateScreen()
    }
}