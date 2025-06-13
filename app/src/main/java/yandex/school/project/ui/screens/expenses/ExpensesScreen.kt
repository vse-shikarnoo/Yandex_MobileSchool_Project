package yandex.school.project.ui.screens.expenses

import androidx.compose.foundation.layout.*
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
fun ExpensesScreen(
    onBtnClick: () -> Unit
) {
    // Мок-данные для расходов
    val expenses = listOf(
        Triple("🏡", "Аренда квартиры", "100 000 ₽"),
        Triple("👗", "Одежда", "100 000 ₽"),
        Triple("🐶", "На собачку\nДжек", "100 000 ₽"),
        Triple("🐶", "На собачку\nЭнни", "100 000 ₽"),
        Triple("PK", "Ремонт квартиры", "100 000 ₽"),
        Triple("🍭", "Продукты", "100 000 ₽"),
        Triple("🏋️", "Спортзал", "100 000 ₽"),
        Triple("💊", "Медицина", "100 000 ₽")
    )
    val total = "436 558 ₽"

    Column(modifier = Modifier.fillMaxSize()) {
        // Блок "Всего"
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
        // Список расходов
        expenses.forEach { (icon, title, amount) ->
            ListItem(
                modifier = Modifier.height(70.dp),
                leadingIcon = icon,
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
fun ExpensesScreenPreview() {
    ProjectTheme {
        Surface {
            ExpensesScreen {}
        }
    }
}