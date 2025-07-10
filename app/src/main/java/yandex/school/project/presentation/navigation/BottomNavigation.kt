package yandex.school.project.presentation.navigation

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import yandex.school.project.R
import androidx.compose.ui.text.input.TextFieldValue
import yandex.school.project.presentation.screens.account.AccountScreen
import yandex.school.project.presentation.screens.category.CategoryScreen
import yandex.school.project.presentation.screens.settings.SettingsScreen
import yandex.school.project.presentation.screens.expenses.create.ExpensesCreateScreen
import yandex.school.project.presentation.screens.expenses.expenses.ExpensesScreen
import yandex.school.project.presentation.screens.expenses.history.ExpensesHistoryScreen
import yandex.school.project.presentation.screens.income.create.IncomesCreateScreen
import yandex.school.project.presentation.screens.income.history.IncomesHistoryScreen
import yandex.school.project.presentation.screens.income.incomes.IncomesScreen

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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IncomesNavGraph(
    accountId: Int,
    currency: String = yandex.school.project.core.utils.CURRENCY_RUB,
    onTitleChange: (yandex.school.project.core.ui.components.TopBarState) -> Unit
) {
    val incomesNavController = rememberNavController()
    val navBackStackEntry by incomesNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val historyIcon = ImageVector.vectorResource(R.drawable.ic_history)
    val analyticIcon = ImageVector.vectorResource(R.drawable.history_analytic)

    // Меняем title в зависимости от текущего route
    LaunchedEffect(currentRoute) {
        when (currentRoute) {
            Destinations.IncomesScreen.route -> {
                onTitleChange(
                    yandex.school.project.core.ui.components.TopBarState(
                        title = "Доходы сегодня",
                        actionIcon = historyIcon,
                        actionIconAction = {
                            incomesNavController.navigate(Destinations.IncomesHistoryScreen.route)
                        },
                        isFAB = true
                    )
                )
            }

            Destinations.IncomesCreateScreen.route -> onTitleChange(
                yandex.school.project.core.ui.components.TopBarState(
                    title = "Мои доходы"
                )
            )

            Destinations.IncomesHistoryScreen.route -> onTitleChange(
                yandex.school.project.core.ui.components.TopBarState(
                    title = "История доходов",
                    actionIcon = analyticIcon,
                    actionIconAction = {

                    },
                    navigationIcon = Icons.Default.ArrowBack,
                    navigationIconAction = {
                        incomesNavController.popBackStack()
                    }
                )
            )
        }
    }

    BackHandler(enabled = incomesNavController.previousBackStackEntry != null) {
        incomesNavController.popBackStack()
    }

    NavHost(
        navController = incomesNavController,
        startDestination = Destinations.IncomesScreen.route
    ) {
        composable(Destinations.IncomesScreen.route) {
            IncomesScreen(accountId = accountId, currency = currency)
        }
        composable(Destinations.IncomesCreateScreen.route) {
            IncomesCreateScreen()
        }
        composable(Destinations.IncomesHistoryScreen.route) {
            IncomesHistoryScreen(accountId = accountId, currency = currency)
        }
    }
} 
