package yandex.school.project.ui.screens.income

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import yandex.school.project.ui.components.ListItem
import yandex.school.project.ui.theme.ProjectTheme

@Composable
fun IncomeScreen() {
    // Мок-данные
    val incomeList = listOf(
        Triple("Зарплата", "500 000 ₽", null),
        Triple("Подработка", "100 000 ₽", null)
    )
    val total = "600 000 ₽"

    Column {


        ListItem(
            modifier = Modifier.height(56.dp),
            contentTitle = "Всего",
            contentSecond = {
                Text(
                    total,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            backgroundColor = MaterialTheme.colorScheme.secondary
        )


        HorizontalDivider()
        // Список доходов
        incomeList.forEach { (title, amount, _) ->
            ListItem(
                modifier = Modifier.height(70.dp),
                contentTitle = title,
                contentSecond = {
                    Text(
                        amount,
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

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun IncomeScreenPreview() {
    ProjectTheme {
        Surface {
            IncomeScreen()
        }
    }
}