package yandex.school.project.feature.settings

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import yandex.school.project.core.R
import yandex.school.project.core.theme.GreenLight
import yandex.school.project.core.theme.GreenMain
import yandex.school.project.core.theme.Grey
import yandex.school.project.core.theme.Pink40
import yandex.school.project.core.theme.Pink80
import yandex.school.project.core.theme.Purple40
import yandex.school.project.core.theme.Purple80
import yandex.school.project.core.theme.PurpleGrey40
import yandex.school.project.core.theme.PurpleGrey80
import yandex.school.project.core.theme.RedMain
import yandex.school.project.core.theme.YellowMain
import yandex.school.project.core.ui.common.ThemeColors
import yandex.school.project.core.utils.PinCodeStorage

fun getAutoSecondaryColor(primary: Color): Color {
    return when (primary) {
        GreenMain -> GreenLight
        Purple80, Purple40 -> PurpleGrey80
        Pink80, Pink40 -> PurpleGrey80
        RedMain -> YellowMain
        YellowMain -> RedMain
        Grey -> PurpleGrey40
        else -> GreenLight
    }
}

@Composable
fun SettingsScreen(
    onThemeChange: (Boolean) -> Unit = {},
    onColorsChange: (ThemeColors) -> Unit = {},
    darkTheme: Boolean = false,
    primaryColor: Color = GreenMain,
    secondaryColor: Color = GreenLight,
    hapticsEnabled: Boolean = true,
    onHapticsChange: (Boolean) -> Unit = {},
) {
    val context = LocalContext.current
    var hasPin by remember { mutableStateOf(PinCodeStorage.hasPin(context)) }
    var showPinSetup by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }
    val colorOptions = listOf(GreenMain, Purple80, Purple40, Pink80, Pink40, RedMain, YellowMain, Grey)
    val logTag = "SettingsScreen"
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Переключатель темы
        yandex.school.project.core.ui.components.ListItem(
            modifier = Modifier.height(56.dp),
            contentTitle = "Тёмная тема",
            trailing = {
                Switch(
                    checked = darkTheme,
                    onCheckedChange = { onThemeChange(it) }
                )
            }
        )
        HorizontalDivider()
        // Выбор основного цвета
        yandex.school.project.core.ui.components.ListItem(
            modifier = Modifier.height(56.dp),
            contentTitle = "Основной цвет",
            trailing = {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(primaryColor)
                        .border(2.dp, Color.Black, CircleShape)
                )
            },
            onClick = { showColorPicker = !showColorPicker }
        )
        if (showColorPicker) {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            ) {
                val selectedBorderColor = if (darkTheme) Color.White else Color.Black
                val unselectedBorderColor = if (darkTheme) Color.DarkGray else Color.LightGray

                colorOptions.forEach { color ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(color)
                            .clickable {
                                Log.d(logTag, "Выбран основной цвет: $color")
                                val secondary = getAutoSecondaryColor(color)
                                Log.d(logTag, "Сохранён secondaryColor: $secondary")
                                onColorsChange(ThemeColors(color, secondary))
                                showColorPicker = false
                            }
                            .border(
                                width = if (color == primaryColor) 3.dp else 1.dp,
                                color = if (color == primaryColor) selectedBorderColor else unselectedBorderColor,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
        HorizontalDivider()
        // Переключатель пин-кода
        yandex.school.project.core.ui.components.ListItem(
            modifier = Modifier.height(56.dp),
            contentTitle = "Код-пароль",
            trailing = {
                Switch(
                    checked = hasPin,
                    onCheckedChange = { checked ->
                        if (checked) {
                            showPinSetup = true
                        } else {
                            PinCodeStorage.clearPin(context)
                            hasPin = false
                        }
                    }
                )
            },
            onClick = {
                if (!hasPin) showPinSetup = true
            }
        )
        HorizontalDivider()
        // Переключатель хаптиков
        yandex.school.project.core.ui.components.ListItem(
            modifier = Modifier.height(56.dp),
            contentTitle = "Хаптики",
            trailing = {
                Switch(
                    checked = hapticsEnabled,
                    onCheckedChange = { onHapticsChange(it) }
                )
            }
        )
        HorizontalDivider()
        // Список пунктов
        val items = listOf(
            "Звуки",
            "Синхронизация",
            "Язык",
            "О программе"
        )
        items.forEach { item ->
            yandex.school.project.core.ui.components.ListItem(
                modifier = Modifier.height(56.dp),
                contentTitle = item,
                trailing = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                        contentDescription = null
                    )
                },
                onClick = { }
            )
            HorizontalDivider()
        }
    }
    if (showPinSetup) {
        PinSetupScreen(
            onPinSet = { pin ->
                PinCodeStorage.savePin(context, pin)
                hasPin = true
                showPinSetup = false
            },
            onCancel = { showPinSetup = false }
        )
    }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun SettingsScreenPreview() {
    yandex.school.project.core.theme.ProjectTheme {
        Surface {
            SettingsScreen()
        }
    }
}