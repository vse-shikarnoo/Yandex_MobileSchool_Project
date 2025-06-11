package yandex.school.project.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import yandex.school.project.R
import yandex.school.project.ui.components.ListItem
import yandex.school.project.ui.theme.ProjectTheme
import yandex.school.project.ui.utils.getInitials

@Composable
fun SettingsScreen() {
    var isAutoTheme by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Переключатель темы
        ListItem(
            contentTitle = "Светлая темная авто",
            trailing = {
                Switch(
                    checked = isAutoTheme,
                    onCheckedChange = { isAutoTheme = it }
                )
            }
        )
        Divider()
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
                leadingIcon = getInitials(item),
                contentTitle = item,
                trailing = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                        contentDescription = null
                    )
                },
                onClick = { /* TODO: обработка нажатия */ }
            )
            Divider()
        }
    }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun SettingsScreenPreview(){
    ProjectTheme {
        Surface {
            SettingsScreen()
        }
    }
}