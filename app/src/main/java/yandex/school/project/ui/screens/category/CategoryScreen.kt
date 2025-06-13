package yandex.school.project.ui.screens.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import yandex.school.project.ui.components.ListItem
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import yandex.school.project.ui.theme.ProjectTheme
import androidx.compose.runtime.collectAsState

// Мок-данные для категорий расходов
private val categories = listOf(
    Triple("🏡", "Аренда квартиры", null),
    Triple("👗", "Одежда", null),
    Triple("🐶", "На собачку", null),
    Triple("🐶", "На собачку", null),
    Triple("PK", "Ремонт квартиры", null),
    Triple("🍭", "Продукты", null),
    Triple("🏋️", "Спортзал", null),
    Triple("💊", "Медицина", null)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel = viewModel()
) {
    var search by remember { mutableStateOf("") }
    val categories by viewModel.categories.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            placeholder = { Text("Найти статью") },
            trailingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            },
            singleLine = true,
            shape = RectangleShape,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )
        Divider()
        categories.forEach { category ->
            ListItem(
                modifier = Modifier.height(70.dp),
                leadingIcon = category.emoji,
                contentTitle = category.name
            )
            Divider()
        }
    }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun CategoryScreenPreview() {
    ProjectTheme {
        Surface {
            CategoryScreen()
        }
    }
}