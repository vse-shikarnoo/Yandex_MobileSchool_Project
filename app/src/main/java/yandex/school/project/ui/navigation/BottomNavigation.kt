package yandex.school.project.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import yandex.school.project.ui.screens.expenses.ExpensesScreen
import yandex.school.project.ui.screens.income.IncomeScreen
import yandex.school.project.ui.screens.account.AccountScreen
import yandex.school.project.ui.screens.expenditure.ExpenditureScreen
import yandex.school.project.ui.screens.expenses.ExpensesCreateScreen
import yandex.school.project.ui.screens.settings.SettingsScreen

@Composable
fun BottomNavigation(
    navController: NavHostController,
    onTitleChange: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarDestinations.Expenses.route
    ) {
        composable(BottomBarDestinations.Expenses.route) { ExpensesNavGraph(onTitleChange) }
        composable(BottomBarDestinations.Income.route) {
            //onTitleChange("Доходы")
            IncomeScreen()
        }
        composable(BottomBarDestinations.Account.route) {
            //onTitleChange("Аккаунт")
            AccountScreen()
        }
        composable(BottomBarDestinations.Expenditure.route) {
            //onTitleChange("Статьи расходов")
            ExpenditureScreen()
        }
        composable(BottomBarDestinations.Settings.route) {
            //onTitleChange("Настройки")
            SettingsScreen()
        }
    }
}

@Composable
fun ExpensesNavGraph(onTitleChange: (String) -> Unit){
    val expensesNavController = rememberNavController()
    val navBackStackEntry by expensesNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Меняем title в зависимости от текущего route
    LaunchedEffect(currentRoute) {
        when (currentRoute) {
            Destinations.ExpensesScreen.route -> onTitleChange("Расходы сегодня")
            Destinations.ExpensesCreateScreen.route -> onTitleChange("Мои расходы")
        }
    }

    BackHandler(enabled = expensesNavController.previousBackStackEntry != null) {
        expensesNavController.popBackStack()
    }

    NavHost(
        navController = expensesNavController,
        startDestination = Destinations.ExpensesScreen.route
    ){
        composable(Destinations.ExpensesScreen.route){
            ExpensesScreen {
                expensesNavController.navigate(Destinations.ExpensesCreateScreen.route)
            }
        }
        composable(Destinations.ExpensesCreateScreen.route) {
            ExpensesCreateScreen()
        }
    }
}