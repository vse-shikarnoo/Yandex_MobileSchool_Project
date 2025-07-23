package yandex.school.project

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import yandex.school.project.core.domain.entities.Account
import yandex.school.project.core.ui.common.ThemeColors
import yandex.school.project.core.ui.components.TopBar
import yandex.school.project.core.ui.components.TopBarState
import yandex.school.project.presentation.navigation.BottomBar
import yandex.school.project.presentation.navigation.BottomNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    account: Account?,
    onThemeChange: (Boolean) -> Unit = {},
    onColorsChange: (ThemeColors) -> Unit = {},
    darkTheme: Boolean = false,
    primaryColor: Color = yandex.school.project.core.theme.GreenMain,
    secondaryColor: Color = yandex.school.project.core.theme.GreenLight,
    hapticsEnabled: Boolean = true,
    onHapticsChange: (Boolean) -> Unit = {},
) {
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
                    onClick = currentTopBarState.value.actionFAB,
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
                onThemeChange = onThemeChange,
                onColorsChange = onColorsChange,
                darkTheme = darkTheme,
                primaryColor = primaryColor,
                secondaryColor = secondaryColor,
                hapticsEnabled = hapticsEnabled,
                onHapticsChange = onHapticsChange
            )
        }
    }
}
