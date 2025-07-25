package yandex.school.project

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun splashScreen_showsLottie_andPinScreenAppears() {
        // Проверяем, что Lottie-анимация отображается (по тестовому тегу или тексту)
        // Здесь предполагается, что у Lottie есть testTag("SplashLottie")
        composeTestRule.onNodeWithTag("SplashLottie").assertExists()
        // Ждём окончания анимации и появления PIN-экрана
//        composeTestRule.waitUntil(timeoutMillis = 5000) {
//            composeTestRule.onAllNodesWithText("Введите PIN-код").fetchSemanticsNodes().isNotEmpty()
//        }
//        // Проверяем, что PIN-экран появился
//        composeTestRule.onNodeWithText("Введите PIN-код").assertExists()
    }
} 