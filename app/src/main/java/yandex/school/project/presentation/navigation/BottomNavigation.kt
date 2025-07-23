package yandex.school.project.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import yandex.school.project.FinanceApplication
import yandex.school.project.R
import yandex.school.project.account.ProvidedAccountScreen
import yandex.school.project.category.ProvidedCategorytScreen
import yandex.school.project.core.ui.common.ThemeColors
import yandex.school.project.expenses.navigation.ExpensesNavGraph
import yandex.school.project.feature.settings.SettingsScreen
import yandex.school.project.income.navigation.IncomesNavGraph

/**
 * Основная навигация нижней панели приложения.
 * Единственная ответственность: настройка и управление навигацией между основными разделами приложения через нижнюю панель.
 */
@Composable
fun BottomNavigation(
    navController: NavHostController,
    onTitleChange: (yandex.school.project.core.ui.components.TopBarState) -> Unit,
    account: yandex.school.project.core.domain.entities.Account?,
    onThemeChange: (Boolean) -> Unit = {},
    onColorsChange: (ThemeColors) -> Unit = {},
    darkTheme: Boolean = false,
    primaryColor: Color = yandex.school.project.core.theme.GreenMain,
    secondaryColor: Color = yandex.school.project.core.theme.GreenLight,
    hapticsEnabled: Boolean = true,
    onHapticsChange: (Boolean) -> Unit = {},
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val appComponent =
        remember { (context.applicationContext as FinanceApplication).appComponent }

    val (currency, setCurrency) = remember { mutableStateOf(account?.currency ?: yandex.school.project.core.utils.CURRENCY_RUB) }
    val (isEditingTitle, setIsEditingTitle) = remember { mutableStateOf(false) }
    val (titleInput, setTitleInput) = remember { mutableStateOf(TextFieldValue(account?.name ?: "Мой счет")) }
    val accountId = account?.id ?: 1

    NavHost(
        navController = navController,
        startDestination = BottomBarDestinations.Expenses.route
    ) {
        composable(BottomBarDestinations.Expenses.route) {

            val expensesComponent = remember { appComponent.expensesComponent().create() }

            ExpensesNavGraph(
                expensesComponent = expensesComponent,
                accountId = accountId,
                currency = currency,
                onTitleChange = onTitleChange
            )
        }
        composable(BottomBarDestinations.Incomes.route) {

            val incomesComponent = remember { appComponent.incomesComponent().create() }

            IncomesNavGraph(
                incomesComponent = incomesComponent,
                accountId = accountId,
                currency = currency,
                onTitleChange = onTitleChange
            )
        }
        composable(BottomBarDestinations.Account.route) {
            val editIcon = ImageVector.vectorResource(R.drawable.ic_edit)
            Log.d("TAG", "BottomNavigation: $account")


//            LaunchedEffect(titleInput, isEditingTitle) {
//                onTitleChange(
//                    yandex.school.project.core.ui.components.TopBarState(
//                        title = titleInput.text,
//                        actionIcon = editIcon,
//                        actionIconAction = { setIsEditingTitle(true) },
//                        isFAB = true,
//                        isEditingTitle = isEditingTitle,
//                        titleInput = titleInput,
//                        onTitleInputChange = { },
//                        onTitleEditDone = { }
//                    )
//                )
//            }

            val accountComponent = remember { appComponent.accountComponent().create() }

            ProvidedAccountScreen(
                accountComponent = accountComponent,
                accountId = accountId,
                currency = currency,
                onCurrencyChanged = { setCurrency(it) }
            )
        }
        composable(BottomBarDestinations.Expenditure.route) {
            onTitleChange(
                yandex.school.project.core.ui.components.TopBarState(
                    title = "Мои статьи"
                )
            )

            val categoryComponent = remember { appComponent.categoryComponent().create() }

            ProvidedCategorytScreen(categoryComponent = categoryComponent)
        }
        composable(BottomBarDestinations.Settings.route) {
            onTitleChange(
                yandex.school.project.core.ui.components.TopBarState(
                    title = "Настройки"
                )
            )
            SettingsScreen(
                onThemeChange = onThemeChange,
                onColorsChange = onColorsChange,
                darkTheme = darkTheme,
                primaryColor = primaryColor,
                secondaryColor = secondaryColor,
                hapticsEnabled = hapticsEnabled,
                onHapticsChange = onHapticsChange
            )
        }
    }
}

