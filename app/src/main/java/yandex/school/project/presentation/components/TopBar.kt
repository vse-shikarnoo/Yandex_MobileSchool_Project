package yandex.school.project.presentation.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

// Состояние для TopBar
data class TopBarState(
    val navigationIcon: ImageVector? = null,
    val navigationIconAction: () -> Unit = {},
    val title: String,
    val actionIcon: ImageVector? = null,
    val actionIconAction: () -> Unit = {},
    val isFAB: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(state: TopBarState) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            if (state.navigationIcon != null) {
                IconButton(onClick = state.navigationIconAction) {
                    Icon(imageVector = state.navigationIcon, contentDescription = null)
                }
            }
        },
        title = {
            Text(
                text = state.title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {
            if (state.actionIcon != null) {
                IconButton(onClick = state.actionIconAction) {
                    Icon(imageVector = state.actionIcon, contentDescription = null)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
} 