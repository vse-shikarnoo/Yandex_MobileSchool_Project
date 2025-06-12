package yandex.school.project.ui.navigation

import androidx.activity.compose.BackHandler
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
import yandex.school.project.ui.screens.expenses.ExpensesScreen
import yandex.school.project.ui.screens.income.IncomeScreen
import yandex.school.project.ui.screens.settings.SettingsScreen

@Composable
fun BottomNavigation(
    navController: NavHostController,
    onTitleChange: (TopBarState) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarDestinations.Expenses.route
    ) {
        composable(BottomBarDestinations.Expenses.route) { ExpensesNavGraph(onTitleChange) }
        composable(BottomBarDestinations.Income.route) {
            onTitleChange(
                TopBarState(
                    navigationIcon = null,
                    title = "Доходы сегодня",
                    actionIcon = ImageVector.vectorResource(R.drawable.ic_history)
                )
            )
            IncomeScreen()
        }
        composable(BottomBarDestinations.Account.route) {
            onTitleChange(
                TopBarState(
                    navigationIcon = null,
                    title = "Мой счет",
                    actionIcon = ImageVector.vectorResource(R.drawable.ic_edit)
                )
            )
            AccountScreen()
        }
        composable(BottomBarDestinations.Expenditure.route) {
            onTitleChange(
                TopBarState(
                    navigationIcon = null,
                    title = "Мои статьи",
                    actionIcon = null
                )
            )
            CategoryScreen()
        }
        composable(BottomBarDestinations.Settings.route) {
            onTitleChange(
                TopBarState(
                    navigationIcon = null,
                    title = "Настройки",
                    actionIcon = null
                )
            )
            SettingsScreen()
        }
    }
}

@Composable
fun ExpensesNavGraph(onTitleChange: (TopBarState) -> Unit) {
    val expensesNavController = rememberNavController()
    val navBackStackEntry by expensesNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Меняем title в зависимости от текущего route
    LaunchedEffect(currentRoute) {
        when (currentRoute) {
            Destinations.ExpensesScreen.route -> {
                onTitleChange(
                    TopBarState(
                        navigationIcon = null,
                        title = "Расходы сегодня",
                        actionIcon = null//ImageVector.vectorResource(R.drawable.ic_history)
                    )
                )
            }

            Destinations.ExpensesCreateScreen.route -> onTitleChange(
                TopBarState(
                    navigationIcon = null,//ImageVector,
                    title = "Мои расходы",
                    actionIcon = null//ImageVector
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
            ExpensesScreen {
                expensesNavController.navigate(Destinations.ExpensesCreateScreen.route)
            }
        }
        composable(Destinations.ExpensesCreateScreen.route) {
            ExpensesCreateScreen()
        }
    }
}