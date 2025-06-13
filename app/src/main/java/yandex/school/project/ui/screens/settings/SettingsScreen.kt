package yandex.school.project.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import yandex.school.project.R
import yandex.school.project.ui.components.ListItem
import yandex.school.project.ui.theme.ProjectTheme

@Composable
fun SettingsScreen() {
    var isAutoTheme by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Переключатель темы
        ListItem(
            modifier = Modifier.height(56.dp),
            contentTitle = "Темная тема",
            trailing = {
                Switch(
                    checked = isAutoTheme,
                    onCheckedChange = { isAutoTheme = it }
                )
            }
        )
        HorizontalDivider()
        // Список пунктов
        val items = listOf(
            "Основной цвет",
            "Звуки",
            "Хаптики",
            "Код пароль",
            "Синхронизация",
            "Язык",
            "О программе"
        )
        items.forEach { item ->
            ListItem(
                modifier = Modifier.height(56.dp),
                contentTitle = item,
                trailing = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                        contentDescription = null
                    )
                },
                onClick = { /* TODO: обработка нажатия */ }
            )
            HorizontalDivider()
        }
    }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun SettingsScreenPreview() {
    ProjectTheme {
        Surface {
            SettingsScreen()
        }
    }
}