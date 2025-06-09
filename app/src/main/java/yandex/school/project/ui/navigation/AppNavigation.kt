package yandex.school.project.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import yandex.school.project.ui.screens.expenses.ExpensesScreen
import yandex.school.project.ui.screens.settings.SettingsScreen
import yandex.school.project.ui.screens.splash.SplashScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Destinations.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = Destinations.Splash.route
        ) {
            SplashScreen(navController = navController) {
                navController.navigate(EXPENSES_ROUTE)
            }
        }
        composable(
            route = EXPENSES_ROUTE
        ) {
            ExpensesScreen()
        }
        composable(
            route = SETTINGS_ROUTE
        ) {
            SettingsScreen()
        }
    }
}