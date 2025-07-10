package yandex.school.project.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import yandex.school.project.R
import yandex.school.project.account.AccountScreen
import yandex.school.project.category.CategoryScreen
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
    account: yandex.school.project.core.domain.entities.Account?
) {
    val (currency, setCurrency) = remember { mutableStateOf(account?.currency ?: yandex.school.project.core.utils.CURRENCY_RUB) }
    val (isEditingTitle, setIsEditingTitle) = remember { mutableStateOf(false) }
    val (titleInput, setTitleInput) = remember { mutableStateOf(TextFieldValue(account?.name ?: "Мой счет")) }
    val accountId = account?.id ?: 1

    NavHost(
        navController = navController,
        startDestination = BottomBarDestinations.Expenses.route
    ) {
        composable(BottomBarDestinations.Expenses.route) {
            ExpensesNavGraph(
                accountId = accountId,
                currency = currency,
                onTitleChange = onTitleChange
            )
        }
        composable(BottomBarDestinations.Incomes.route) {
            IncomesNavGraph(
                accountId = accountId,
                currency = currency,
                onTitleChange = onTitleChange
            )
        }
        composable(BottomBarDestinations.Account.route) {
            val editIcon = ImageVector.vectorResource(R.drawable.ic_edit)
            Log.d("TAG", "BottomNavigation: $account")

            LaunchedEffect(titleInput, isEditingTitle) {
                onTitleChange(
                    yandex.school.project.core.ui.components.TopBarState(
                        title = titleInput.text,
                        actionIcon = editIcon,
                        actionIconAction = { setIsEditingTitle(true) },
                        isFAB = true,
                        isEditingTitle = isEditingTitle,
                        titleInput = titleInput,
                        onTitleInputChange = { },
                        onTitleEditDone = { }
                    )
                )
            }

            AccountScreen(
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
            CategoryScreen()
        }
        composable(BottomBarDestinations.Settings.route) {
            onTitleChange(
                yandex.school.project.core.ui.components.TopBarState(
                    title = "Настройки"
                )
            )
            SettingsScreen()
        }
    }
}

