package yandex.school.project.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import yandex.school.project.MainScreen
import yandex.school.project.splash.SplashScreen

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
    val (account, setAccount) = remember { mutableStateOf<yandex.school.project.core.domain.entities.Account?>(null) }
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
                accountChange = { setAccount(it) }
            )
        }
        composable(MainDestinations.Main) {
            MainScreen(account = account)
        }
    }
} 