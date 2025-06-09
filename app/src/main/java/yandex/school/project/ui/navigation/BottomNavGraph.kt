package yandex.school.project.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import yandex.school.project.ui.screens.expenses.ExpensesScreen
import yandex.school.project.ui.screens.income.IncomeScreen
import yandex.school.project.ui.screens.account.AccountScreen
import yandex.school.project.ui.screens.expenditure.ExpenditureScreen
import yandex.school.project.ui.screens.settings.SettingsScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarDestinations.Expenses.route
    ) {
        composable(BottomBarDestinations.Expenses.route) { ExpensesScreen() }
        composable(BottomBarDestinations.Income.route) { IncomeScreen() }
        composable(BottomBarDestinations.Account.route) { AccountScreen() }
        composable(BottomBarDestinations.Expenditure.route) { ExpenditureScreen() }
        composable(BottomBarDestinations.Settings.route) { SettingsScreen() }
    }
} 