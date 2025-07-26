package yandex.school.project.core.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class PinEnterScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun pinEnterScreen_correctPinInput_triggersCallback() {
        var pinEntered: String? = null
        composeTestRule.setContent {
            PinEnterScreen(onPinEntered = { pinEntered = it })
        }
        composeTestRule.onNodeWithText("PIN-код").performTextInput("1234")
        composeTestRule.onNodeWithText("Готово").performClick()
        assert(pinEntered == "1234")
    }

    @Test
    fun pinEnterScreen_incorrectPinLength_showsError() {
        composeTestRule.setContent {
            PinEnterScreen(onPinEntered = {})
        }
        composeTestRule.onNodeWithText("PIN-код").performTextInput("12")
        composeTestRule.onNodeWithText("Готово").performClick()
        composeTestRule.onNodeWithText("Введите 4 цифры").assertExists()
    }
} 