package yandex.school.project.income.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import yandex.school.project.incomes.R
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import yandex.school.project.income.IncomesScreen
import yandex.school.project.income.create.IncomesCreateScreen
import yandex.school.project.income.history.IncomesHistoryScreen


/**
 * Навигация для раздела доходов.
 * Единственная ответственность: управление навигацией между экранами доходов.
 */
@Composable
fun IncomesNavGraph(
    accountId: Int,
    currency: String = yandex.school.project.core.utils.CURRENCY_RUB,
    onTitleChange: (yandex.school.project.core.ui.components.TopBarState) -> Unit
) {
    val incomesNavController = rememberNavController()
    val navBackStackEntry by incomesNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val historyIcon = ImageVector.vectorResource(R.drawable.ic_history)
    val analyticIcon = ImageVector.vectorResource(R.drawable.history_analytic)

    // Меняем title в зависимости от текущего route
    LaunchedEffect(currentRoute) {
        when (currentRoute) {
            IncomesDestinations.IncomesScreen.route -> {
                onTitleChange(
                    yandex.school.project.core.ui.components.TopBarState(
                        title = "Доходы сегодня",
                        actionIcon = historyIcon,
                        actionIconAction = {
                            incomesNavController.navigate(IncomesDestinations.IncomesHistoryScreen.route)
                        },
                        isFAB = true
                    )
                )
            }

            IncomesDestinations.IncomesCreateScreen.route -> onTitleChange(
                yandex.school.project.core.ui.components.TopBarState(
                    title = "Мои доходы"
                )
            )

            IncomesDestinations.IncomesHistoryScreen.route -> onTitleChange(
                yandex.school.project.core.ui.components.TopBarState(
                    title = "История доходов",
                    actionIcon = analyticIcon,
                    actionIconAction = {

                    },
                    navigationIcon = Icons.Default.ArrowBack,
                    navigationIconAction = {
                        incomesNavController.popBackStack()
                    }
                )
            )
        }
    }

    BackHandler(enabled = incomesNavController.previousBackStackEntry != null) {
        incomesNavController.popBackStack()
    }

    NavHost(
        navController = incomesNavController,
        startDestination = IncomesDestinations.IncomesScreen.route
    ) {
        composable(IncomesDestinations.IncomesScreen.route) {
            IncomesScreen(accountId = accountId, currency = currency)
        }
        composable(IncomesDestinations.IncomesCreateScreen.route) {
            IncomesCreateScreen()
        }
        composable(IncomesDestinations.IncomesHistoryScreen.route) {
            IncomesHistoryScreen(accountId = accountId, currency = currency)
        }
    }
}