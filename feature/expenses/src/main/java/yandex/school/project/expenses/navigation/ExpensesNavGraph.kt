package yandex.school.project.expenses.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import yandex.school.project.core.ui.components.TopBarState
import yandex.school.project.core.utils.CURRENCY_RUB
import yandex.school.project.expenses.R
import yandex.school.project.expenses.di.ExpensesComponent
import yandex.school.project.expenses.di.LocalExpensesViewModelFactory
import yandex.school.project.expenses.edit.ExpensesEditScreen
import yandex.school.project.expenses.history.ExpensesHistoryScreen
import yandex.school.project.income.ExpensesScreen

/**
 * Навигация для раздела расходов.
 * Единственная ответственность: управление навигацией между экранами расходов.
 */
@Composable
fun ExpensesNavGraph(
    expensesComponent: ExpensesComponent,
    modifier: Modifier = Modifier,
    accountId: Int,
    currency: String = CURRENCY_RUB,
    onTitleChange: (TopBarState) -> Unit
) {
    val expensesNavController = rememberNavController()
    val navBackStackEntry by expensesNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val historyIcon = ImageVector.vectorResource(R.drawable.ic_history)
    val analyticIcon = ImageVector.vectorResource(R.drawable.history_analytic)
    val closeIcon = Icons.Default.Close

    // Меняем title в зависимости от текущего route
    LaunchedEffect(currentRoute) {
        when (currentRoute) {
            ExpensesDestinations.ExpensesScreen.route -> {
                onTitleChange(
                    TopBarState(
                        title = "Расходы сегодня",
                        actionIcon = historyIcon,
                        actionIconAction = {
                            expensesNavController.navigate(ExpensesDestinations.ExpensesHistoryScreen.route)
                        },
                        isFAB = true,
                        actionFAB = {
                            expensesNavController.navigate(ExpensesDestinations.ExpensesEditScreen.route)
                        }
                    )
                )
            }

            ExpensesDestinations.ExpensesEditScreen.route -> onTitleChange(
                TopBarState(
                    title = "Мои расходы",
                    navigationIcon = closeIcon,
                    navigationIconAction = {
                        expensesNavController.popBackStack()
                    }
                )
            )
            // Для маршрута с id
            ExpensesDestinations.ExpensesEditScreen.route + "/{transactionId}" -> onTitleChange(
                TopBarState(
                    title = "Мои расходы",
                    navigationIcon = closeIcon,
                    navigationIconAction = {
                        expensesNavController.popBackStack()
                    }
                )
            )

            ExpensesDestinations.ExpensesHistoryScreen.route -> onTitleChange(
                TopBarState(
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

    val viewModelFactory = remember { expensesComponent.viewModelFactory() }
    CompositionLocalProvider(LocalExpensesViewModelFactory provides viewModelFactory) {
        NavHost(
            navController = expensesNavController,
            startDestination = ExpensesDestinations.ExpensesScreen.route
        ) {
            composable(ExpensesDestinations.ExpensesScreen.route) {
                ExpensesScreen(
                    modifier = modifier,
                    accountId = accountId,
                    currency = currency,
                    onClickEdit = { transactionId ->
                        expensesNavController.navigate(
                            ExpensesDestinations.ExpensesEditScreen.route + "/$transactionId"
                        )
                    }
                )
            }
            composable(ExpensesDestinations.ExpensesEditScreen.route) {
                ExpensesEditScreen(
                    modifier = modifier,
                    accountId = accountId,
                    isEditMode = false
                ) {
                    expensesNavController.navigate(ExpensesDestinations.ExpensesScreen.route) {
                        popUpTo(ExpensesDestinations.ExpensesScreen.route) { inclusive = true }
                    }
                }
            }
            composable(
                ExpensesDestinations.ExpensesEditScreen.route + "/{transactionId}"
            ) { backStackEntry ->
                val transactionId = backStackEntry.arguments?.getString("transactionId")?.toIntOrNull()
                ExpensesEditScreen(
                    modifier = modifier,
                    accountId = accountId,
                    isEditMode = true,
                    transactionId = transactionId
                ) {
                    expensesNavController.navigate(ExpensesDestinations.ExpensesScreen.route) {
                        popUpTo(ExpensesDestinations.ExpensesScreen.route) { inclusive = true }
                    }
                }
            }
            composable(ExpensesDestinations.ExpensesHistoryScreen.route) {
                ExpensesHistoryScreen(
                    modifier = modifier,
                    accountId = accountId,
                    currency = currency
                )
            }
        }
    }
}