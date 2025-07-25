package yandex.school.project.feature.settings

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import yandex.school.project.core.R
import yandex.school.project.core.data.local.UserPreferences
import yandex.school.project.core.data.local.UserPreferencesDataStore
import yandex.school.project.core.ui.components.ColorPicker
import yandex.school.project.core.utils.PinCodeStorage
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val dataStore = remember { UserPreferencesDataStore(context) }
    val userPrefs by dataStore.preferencesFlow.collectAsState(
        initial = UserPreferences(
            0xFF2AE881UL,
            0xFFD4FAE6UL,
            false
        )
    )
    // Логирование текущих настроек
    Log.d(
        "SettingsScreen",
        "userPrefs: primaryColor=0x${userPrefs.primaryColor.toString(16)}, secondaryColor=0x${
            userPrefs.secondaryColor.toString(16)
        }, darkTheme=${userPrefs.darkTheme}"
    )
    val coroutineScope = rememberCoroutineScope()
    var isAutoTheme by remember { mutableStateOf(userPrefs.darkTheme) }
    var hasPin by remember { mutableStateOf(false) }
    var showPinSetup by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }

    LaunchedEffect(userPrefs.darkTheme) { isAutoTheme = userPrefs.darkTheme }
    LaunchedEffect(Unit) {
        hasPin = PinCodeStorage.hasPin(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Переключатель темы
        yandex.school.project.core.ui.components.ListItem(
            modifier = Modifier.height(56.dp),
            contentTitle = "Темная тема",
            trailing = {
                Switch(
                    checked = isAutoTheme,
                    onCheckedChange = {
                        isAutoTheme = it
                        coroutineScope.launch { dataStore.updateDarkTheme(it) }
                    },
                    modifier = Modifier.testTag("ThemeSwitch")
                )
            }
        )
        HorizontalDivider()
        // Цвета
        yandex.school.project.core.ui.components.ListItem(
            modifier = Modifier.height(56.dp),
            contentTitle = "Основной цвет",
            trailing = {
                Box(
                    Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color((userPrefs.primaryColor and 0xFFFFFFFFUL).toLong()))
                )
            },
            onClick = { showColorPicker = !showColorPicker }
        )
        if (showColorPicker) {
            ColorPicker(
                selectedColor = userPrefs.primaryColor,
                onColorSelected = {
                    Log.d(
                        "ColorPicker",
                        "Выбран цвет: primary=0x${it.primary.toString(16)}, secondary=0x${
                            it.secondary.toString(16)
                        }"
                    )
                    coroutineScope.launch {
                        dataStore.updateColors(it.primary, it.secondary)
                        showColorPicker = false
                    }
                },
                darkTheme = userPrefs.darkTheme
            )
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
                            yandex.school.project.core.utils.PinCodeStorage.clearPin(context)
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
        // Список пунктов
        val items = listOf(
            "Звуки",
            "Хаптики",
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
        // --- Добавлено: версия и дата обновления ---
        val (versionName, lastUpdate) = remember {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            val date = Date(pInfo.lastUpdateTime)
            val version = pInfo.versionName ?: "?"
            val formattedDate = SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault()).format(date)
            version to formattedDate
        }
        androidx.compose.material3.Text(
            text = "Версия: $versionName\nОбновлено: $lastUpdate",
            modifier = Modifier
                .padding(vertical = 32.dp, horizontal = 16.dp)
                .fillMaxSize(),
            color = Color.Gray
        )
        // --- конец добавления ---
    }
    if (showPinSetup) {
        Dialog(onDismissRequest = { showPinSetup = false }) {
            androidx.compose.material3.Surface(
                shape = RoundedCornerShape(28.dp),
                tonalElevation = 8.dp,
                color = androidx.compose.material3.MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .widthIn(min = 280.dp, max = 360.dp)
            ) {
                PinSetupScreen(
                    onPinSet = { pin ->
                        yandex.school.project.core.utils.PinCodeStorage.savePin(context, pin)
                        hasPin = true
                        showPinSetup = false
                    },
                    onCancel = { showPinSetup = false },
                    buttonShape = RoundedCornerShape(50)
                )

            }
        }
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