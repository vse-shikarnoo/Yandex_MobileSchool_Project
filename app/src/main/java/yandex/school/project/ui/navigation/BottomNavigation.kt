package yandex.school.project.ui.navigation

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import yandex.school.project.R
import yandex.school.project.ui.screens.TopBarState
import yandex.school.project.ui.screens.account.AccountScreen
import yandex.school.project.ui.screens.category.CategoryScreen
import yandex.school.project.ui.screens.expenses.ExpensesCreateScreen
import yandex.school.project.ui.screens.expenses.ExpensesHistoryScreen
import yandex.school.project.ui.screens.expenses.ExpensesScreen
import yandex.school.project.ui.screens.income.IncomesCreateScreen
import yandex.school.project.ui.screens.income.IncomesHistoryScreen
import yandex.school.project.ui.screens.income.IncomesScreen
import yandex.school.project.ui.screens.settings.SettingsScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavigation(
    navController: NavHostController,
    onTitleChange: (TopBarState) -> Unit,
    accountId: Int
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarDestinations.Expenses.route
    ) {
        composable(BottomBarDestinations.Expenses.route) {
            ExpensesNavGraph(
                accountId = accountId,
                onTitleChange
            )
        }
        composable(BottomBarDestinations.Incomes.route) {
            IncomesNavGraph(accountId = accountId, onTitleChange)
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
            AccountScreen()
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpensesNavGraph(
    accountId: Int,
    onTitleChange: (TopBarState) -> Unit
) {
    val expensesNavController = rememberNavController()
    val navBackStackEntry by expensesNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val historyIcon = ImageVector.vectorResource(R.drawable.ic_history)

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
                    title = "Мои расходы"
                )
            )

            Destinations.ExpensesHistoryScreen.route -> onTitleChange(
                TopBarState(
                    title = "История расходов"
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
            ExpensesScreen(accountId = accountId)
        }
        composable(Destinations.ExpensesCreateScreen.route) {
            ExpensesCreateScreen()
        }
        composable(Destinations.ExpensesHistoryScreen.route) {
            ExpensesHistoryScreen(accountId = accountId)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IncomesNavGraph(
    accountId: Int,
    onTitleChange: (TopBarState) -> Unit
) {
    val incomesNavController = rememberNavController()
    val navBackStackEntry by incomesNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val historyIcon = ImageVector.vectorResource(R.drawable.ic_history)

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
                    title = "История доходов"
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
            IncomesScreen(accountId = accountId)
        }
        composable(Destinations.IncomesCreateScreen.route) {
            IncomesCreateScreen()
        }
        composable(Destinations.IncomesHistoryScreen.route) {
            IncomesHistoryScreen(accountId = accountId)
        }
    }
}