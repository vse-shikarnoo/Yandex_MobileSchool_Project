package yandex.school.project.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import yandex.school.project.ui.screens.home.HomeScreen
import yandex.school.project.ui.screens.splash.SplashScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screens.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = Screens.Splash.route
        ) {
            SplashScreen(navController = navController) {
                navController.navigate(Screens.Home.route)
            }
        }

        composable(
            route = Screens.Home.route
        ) {
            HomeScreen()
        }
    }
}