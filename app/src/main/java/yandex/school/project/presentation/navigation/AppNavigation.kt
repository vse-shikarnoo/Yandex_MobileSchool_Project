package yandex.school.project.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import yandex.school.project.presentation.screens.MainScreen
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import yandex.school.project.presentation.screens.splash.SplashScreen

/**
 * Константы для основных экранов приложения.
 * Единственная ответственность: хранение констант для навигации между основными экранами.
 */
object MainDestinations {
    const val Splash = "splash_screen"
    const val Main = "main_screen"
}

/**
 * Основная навигация приложения, управляющая переходами между экранами.
 * Единственная ответственность: настройка и управление навигацией между экранами приложения.
 */
@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    val (accountId, setAccountId) = remember { mutableStateOf<Int>(1) }
    NavHost(
        navController = navController,
        startDestination = MainDestinations.Splash
    ) {
        composable(MainDestinations.Splash) {
            SplashScreen(
                goNextDestination = {
                    navController.navigate(MainDestinations.Main) {
                        popUpTo(MainDestinations.Splash) { inclusive = true }
                    }
                },
                accountIdChange = { setAccountId(it) }
            )
        }
        composable(MainDestinations.Main) {
            MainScreen(accountId = accountId)
        }
    }
} 