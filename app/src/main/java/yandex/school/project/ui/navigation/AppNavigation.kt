package yandex.school.project.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import yandex.school.project.ui.screens.MainScreen
import yandex.school.project.ui.screens.splash.SplashScreen

object MainDestinations {
    const val Splash = "splash_screen"
    const val Main = "main_screen"
}

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = MainDestinations.Splash
    ) {
        composable(MainDestinations.Splash) {
            SplashScreen(navController = navController) {
                navController.navigate(MainDestinations.Main) {
                    popUpTo(MainDestinations.Splash) { inclusive = true }
                }
            }
        }
        composable(MainDestinations.Main) {
            MainScreen(navController)
        }
    }
}