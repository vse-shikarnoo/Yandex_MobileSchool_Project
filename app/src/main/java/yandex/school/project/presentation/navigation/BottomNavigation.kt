package yandex.school.project.presentation.navigation

import android.os.Build
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
import yandex.school.project.presentation.components.TopBarState
import yandex.school.project.presentation.screens.account.AccountScreen
import yandex.school.project.presentation.screens.category.CategoryScreen
import yandex.school.project.presentation.screens.expenses.create.ExpensesCreateScreen
import yandex.school.project.presentation.screens.expenses.history.ExpensesHistoryScreen
import yandex.school.project.presentation.screens.expenses.expenses.ExpensesScreen
import yandex.school.project.presentation.screens.income.create.IncomesCreateScreen
import yandex.school.project.presentation.screens.income.history.IncomesHistoryScreen
import yandex.school.project.presentation.screens.income.incomes.IncomesScreen
import yandex.school.project.presentation.screens.settings.SettingsScreen
import yandex.school.project.presentation.utils.CURRENCY_RUB

/**
 * Основная навигация нижней панели приложения.
 * Единственная ответственность: настройка и управление навигацией между основными разделами приложения через нижнюю панель.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavigation(
    navController: NavHostController,
    onTitleChange: (TopBarState) -> Unit,
    accountId: Int,
    _currency: String
) {
    val (currency, setCurrency) = remember { mutableStateOf(_currency) }
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
            IncomesNavGraph(accountId = accountId, currency = currency, onTitleChange = onTitleChange)
        }
        composable(BottomBarDestinations.Account.route) {
            val editIcon = ImageVector.vectorResource(R.drawable.ic_edit)
            onTitleChange(
                TopBarState(
                    title = "Мой счет",
                    actionIcon = editIcon,
                    isFAB = true
                )
            )
            AccountScreen(accountId = accountId, currency = currency, onCurrencyChanged = {setCurrency(it)})
        }
        composable(BottomBarDestinations.Expenditure.route) {
            onTitleChange(
                TopBarState(
                    title = "Мои статьи"
                )
            )
            CategoryScreen()
        }
        composable(BottomBarDestinations.Settings.route) {
            onTitleChange(
                TopBarState(
                    title = "Настройки"
                )
            )
            SettingsScreen()
        }
    }
}

/**
 * Навигация для раздела расходов.
 * Единственная ответственность: управление навигацией между экранами расходов.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpensesNavGraph(
    accountId: Int,
    currency: String = CURRENCY_RUB,
    onTitleChange: (TopBarState) -> Unit
) {
    val expensesNavController = rememberNavController()
    val navBackStackEntry by expensesNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val historyIcon = ImageVector.vectorResource(R.drawable.ic_history)
    val analyticIcon = ImageVector.vectorResource(R.drawable.history_analytic)

    // Меняем title в зависимости от текущего route
    LaunchedEffect(currentRoute) {
        when (currentRoute) {
            Destinations.ExpensesScreen.route -> {
                onTitleChange(
                    TopBarState(
                        title = "Расходы сегодня",
                        actionIcon = historyIcon,
                        actionIconAction = {
                            expensesNavController.navigate(Destinations.ExpensesHistoryScreen.route)
                        },
                        isFAB = true
                    )
                )
            }

            Destinations.ExpensesCreateScreen.route -> onTitleChange(
                TopBarState(
                    title = "Мои расходы",
                )
            )

            Destinations.ExpensesHistoryScreen.route -> onTitleChange(
                TopBarState(
                    title = "История расходов",
                    actionIcon = analyticIcon,
                    actionIconAction = {

                    },
                    navigationIcon = Icons.Default.ArrowBack,
                    navigationIconAction = {
                        expensesNavController.popBackStack()
                    }
                )
            )
        }
    }

    BackHandler(enabled = expensesNavController.previousBackStackEntry != null) {
        expensesNavController.popBackStack()
    }

    NavHost(
        navController = expensesNavController,
        startDestination = Destinations.ExpensesScreen.route
    ) {
        composable(Destinations.ExpensesScreen.route) {
            ExpensesScreen(accountId = accountId, currency = currency)
        }
        composable(Destinations.ExpensesCreateScreen.route) {
            ExpensesCreateScreen()
        }
        composable(Destinations.ExpensesHistoryScreen.route) {
            ExpensesHistoryScreen(accountId = accountId, currency = currency)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IncomesNavGraph(
    accountId: Int,
    currency: String = CURRENCY_RUB,
    onTitleChange: (TopBarState) -> Unit
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
                    TopBarState(
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
                TopBarState(
                    title = "Мои доходы"
                )
            )

            Destinations.IncomesHistoryScreen.route -> onTitleChange(
                TopBarState(
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
            IncomesHistoryScreen(accountId = accountId)
        }
    }
} 