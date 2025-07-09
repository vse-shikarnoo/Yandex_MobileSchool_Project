package yandex.school.project.presentation.screens.income.create

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import yandex.school.project.core.theme.ProjectTheme

@Composable
fun IncomesCreateScreen() {
    // TODO: Реализовать экран создания доходов
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "IncomesCreate")
    }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun IncomesCreateScreenPreview() {
    yandex.school.project.core.theme.ProjectTheme {
        IncomesCreateScreen()
    }
}