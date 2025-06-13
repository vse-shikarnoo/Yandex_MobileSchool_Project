package yandex.school.project.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import yandex.school.project.ui.navigation.BottomBar
import yandex.school.project.ui.navigation.BottomNavigation
import yandex.school.project.ui.theme.ProjectTheme

data class TopBarState(
    val navigationIcon: ImageVector?,
    val navigationIconAction: () -> Unit = {},
    val title: String,
    val actionIcon: ImageVector?,
    val actionIconAction: () -> Unit = {},
    val isFAB: Boolean = false
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
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
            CenterAlignedTopAppBar(
                navigationIcon = {
                    if (currentTopBarState.value.navigationIcon != null) {
                        IconButton(
                            onClick = currentTopBarState.value.navigationIconAction
                        ) {
                            Icon(
                                imageVector = currentTopBarState.value.navigationIcon!!,
                                null
                            )
                        }
                    }
                },
                title = {
                    Text(
                        text = currentTopBarState.value.title,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    if (currentTopBarState.value.actionIcon != null) {
                        IconButton(
                            onClick = currentTopBarState.value.actionIconAction
                        ) {
                            Icon(
                                imageVector = currentTopBarState.value.actionIcon!!,
                                null
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
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
                onTitleChange = { currentTopBarState.value = it }
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun MainScreenPreview() {
    ProjectTheme {
        MainScreen()
    }
}