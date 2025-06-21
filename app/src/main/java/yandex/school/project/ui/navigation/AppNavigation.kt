package yandex.school.project.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import yandex.school.project.ui.screens.MainScreen
import yandex.school.project.ui.screens.splash.SplashScreen
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import yandex.school.project.ui.screens.splash.SplashViewModel

object MainDestinations {
    const val Splash = "splash_screen"
    const val Main = "main_screen"
}

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    val (accountId, setAccountId) = remember { mutableStateOf<Int>(1) }
    NavHost(
        navController = navController,
        startDestination = MainDestinations.Splash
    ) {
        composable(MainDestinations.Splash) {
            SplashScreen(
                navController = navController,
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