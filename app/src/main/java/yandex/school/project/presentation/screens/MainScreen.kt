package yandex.school.project.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import yandex.school.project.presentation.components.TopBar
import yandex.school.project.presentation.components.TopBarState
import yandex.school.project.presentation.navigation.BottomBar
import yandex.school.project.presentation.navigation.BottomNavigation
import yandex.school.project.presentation.theme.ProjectTheme
import yandex.school.project.presentation.screens.account.AccountViewModel
import yandex.school.project.presentation.common.Result
import yandex.school.project.domain.entities.Account
import yandex.school.project.presentation.utils.CURRENCY_RUB

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(account: Account?) {
    val bottomNavController = rememberNavController()
    val currentTopBarState = remember {
        mutableStateOf(
            TopBarState(
                navigationIcon = null,
                title = "Расходы сегодня",
                actionIcon = null,
            )
        )
    }
    Scaffold(
        topBar = {
            TopBar(currentTopBarState.value)
        },
        bottomBar = { BottomBar(bottomNavController) },
        floatingActionButton = {
            if (currentTopBarState.value.isFAB) {
                FloatingActionButton(
                    modifier = Modifier.clip(CircleShape),
                    onClick = {},
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            BottomNavigation(
                navController = bottomNavController,
                onTitleChange = { currentTopBarState.value = it },
                account = account,
            )
        }
    }
}
