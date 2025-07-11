package yandex.school.project.income.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import yandex.school.project.expenses.edit.IncomesEditScreen
import yandex.school.project.income.IncomesScreen
import yandex.school.project.income.di.IncomesComponent
import yandex.school.project.income.di.LocalIncomesViewModelFactory
import yandex.school.project.income.history.IncomesHistoryScreen
import yandex.school.project.incomes.R


/**
 * Навигация для раздела доходов.
 * Единственная ответственность: управление навигацией между экранами доходов.
 */
@Composable
fun IncomesNavGraph(
    incomesComponent: IncomesComponent,
    accountId: Int,
    currency: String = yandex.school.project.core.utils.CURRENCY_RUB,
    onTitleChange: (yandex.school.project.core.ui.components.TopBarState) -> Unit
) {
    val incomesNavController = rememberNavController()
    val navBackStackEntry by incomesNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val historyIcon = ImageVector.vectorResource(R.drawable.ic_history)
    val analyticIcon = ImageVector.vectorResource(R.drawable.history_analytic)
    val closeIcon = Icons.Default.ArrowBack

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
                        isFAB = true,
                        actionFAB = {
                            incomesNavController.navigate(IncomesDestinations.IncomesEditScreen.route)
                        }
                    )
                )
            }
            IncomesDestinations.IncomesEditScreen.route -> onTitleChange(
                yandex.school.project.core.ui.components.TopBarState(
                    title = "Создать доход",
                    navigationIcon = closeIcon,
                    navigationIconAction = {
                        incomesNavController.popBackStack()
                    }
                )
            )
            IncomesDestinations.IncomesEditScreen.route + "/{incomeId}" -> onTitleChange(
                yandex.school.project.core.ui.components.TopBarState(
                    title = "Редактировать доход",
                    navigationIcon = closeIcon,
                    navigationIconAction = {
                        incomesNavController.popBackStack()
                    }
                )
            )
            IncomesDestinations.IncomesHistoryScreen.route -> onTitleChange(
                yandex.school.project.core.ui.components.TopBarState(
                    title = "История доходов",
                    actionIcon = analyticIcon,
                    actionIconAction = {},
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

    val viewModelFactory = remember { incomesComponent.viewModelFactory() }
    CompositionLocalProvider(LocalIncomesViewModelFactory provides viewModelFactory) {
        NavHost(
            navController = incomesNavController,
            startDestination = IncomesDestinations.IncomesScreen.route
        ) {
            composable(IncomesDestinations.IncomesScreen.route) {
                IncomesScreen(
                    accountId = accountId,
                    currency = currency,
                    onClickEdit = { incomeId ->
                        incomesNavController.navigate(IncomesDestinations.IncomesEditScreen.route + "/$incomeId")
                    }
                )
            }
            composable(IncomesDestinations.IncomesEditScreen.route) {
                IncomesEditScreen(
                    accountId = accountId,
                    isEditMode = false
                ) {
                    incomesNavController.navigate(IncomesDestinations.IncomesScreen.route) {
                        popUpTo(IncomesDestinations.IncomesScreen.route) { inclusive = true }
                    }
                }
            }
            composable(IncomesDestinations.IncomesEditScreen.route + "/{transactionId}") { backStackEntry ->
                val transactionId = backStackEntry.arguments?.getString("transactionId")?.toIntOrNull()
                IncomesEditScreen(
                    accountId = accountId,
                    isEditMode = true,
                    transactionId = transactionId
                ) {
                    incomesNavController.navigate(IncomesDestinations.IncomesScreen.route) {
                        popUpTo(IncomesDestinations.IncomesScreen.route) { inclusive = true }
                    }
                }
            }
            composable(IncomesDestinations.IncomesHistoryScreen.route) {
                IncomesHistoryScreen(accountId = accountId, currency = currency)
            }
        }
    }
}