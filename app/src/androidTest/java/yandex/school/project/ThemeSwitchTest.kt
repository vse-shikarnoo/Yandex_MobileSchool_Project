package yandex.school.project

import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ThemeSwitchTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun themeSwitch_changesTheme() {
        // Кликаем по иконке/кнопке "Настройки" в BottomBar
        composeTestRule.onNodeWithTag("Settings").performClick()
        // Проверяем, что Switch отображается
        val switch = composeTestRule.onNodeWithTag("ThemeSwitch")
        switch.assertExists()
        // Запоминаем состояние до
        val isCheckedBefore = switch.fetchSemanticsNode().config.getOrNull(androidx.compose.ui.semantics.SemanticsProperties.ToggleableState)
        // Переключаем тему
        switch.performClick()
        // Проверяем, что состояние изменилось
        val isCheckedAfter = switch.fetchSemanticsNode().config.getOrNull(androidx.compose.ui.semantics.SemanticsProperties.ToggleableState)
        assert(isCheckedBefore != isCheckedAfter)
    }
}