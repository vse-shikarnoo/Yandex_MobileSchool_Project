package yandex.school.project.expenses.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import yandex.school.project.expenses.ExpensesScreen
import yandex.school.project.expenses.R
import yandex.school.project.expenses.create.ExpensesCreateScreen
import yandex.school.project.expenses.history.ExpensesHistoryScreen

/**
 * Навигация для раздела расходов.
 * Единственная ответственность: управление навигацией между экранами расходов.
 */
@Composable
fun ExpensesNavGraph(
    modifier: Modifier = Modifier,
    accountId: Int,
    currency: String = yandex.school.project.core.utils.CURRENCY_RUB,
    onTitleChange: (yandex.school.project.core.ui.components.TopBarState) -> Unit
) {
    val expensesNavController = rememberNavController()
    val navBackStackEntry by expensesNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val historyIcon = ImageVector.vectorResource(R.drawable.ic_history)
    val analyticIcon = ImageVector.vectorResource(R.drawable.history_analytic)

    // Меняем title в зависимости от текущего route
    LaunchedEffect(currentRoute) {
        when (currentRoute) {
            ExpensesDestinations.ExpensesScreen.route -> {
                onTitleChange(
                    yandex.school.project.core.ui.components.TopBarState(
                        title = "Расходы сегодня",
                        actionIcon = historyIcon,
                        actionIconAction = {
                            expensesNavController.navigate(ExpensesDestinations.ExpensesHistoryScreen.route)
                        },
                        isFAB = true
                    )
                )
            }

            ExpensesDestinations.ExpensesCreateScreen.route -> onTitleChange(
                yandex.school.project.core.ui.components.TopBarState(
                    title = "Мои расходы",
                )
            )

            ExpensesDestinations.ExpensesHistoryScreen.route -> onTitleChange(
                yandex.school.project.core.ui.components.TopBarState(
                    title = "История расходов",
                    actionIcon = analyticIcon,
                    actionIconAction = {

                    },
                    navigationIcon = Icons.Default.ArrowBack,
                    navigationIconAction = {
                        expensesNavController.popBackStack()
                    }
                )
            )
        }
    }

    BackHandler(enabled = expensesNavController.previousBackStackEntry != null) {
        expensesNavController.popBackStack()
    }

    NavHost(
        navController = expensesNavController,
        startDestination = ExpensesDestinations.ExpensesScreen.route
    ) {
        composable(ExpensesDestinations.ExpensesScreen.route) {
            ExpensesScreen(modifier = modifier, accountId = accountId, currency = currency)
        }
        composable(ExpensesDestinations.ExpensesCreateScreen.route) {
            ExpensesCreateScreen(modifier = modifier)
        }
        composable(ExpensesDestinations.ExpensesHistoryScreen.route) {
            ExpensesHistoryScreen(modifier = modifier, accountId = accountId, currency = currency)
        }
    }
}