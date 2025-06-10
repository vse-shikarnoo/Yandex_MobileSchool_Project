package yandex.school.project.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import yandex.school.project.ui.theme.ProjectTheme

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    leading: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
    trailing: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leading != null) {
            leading()
            Spacer(modifier = Modifier.width(16.dp))
        }
        Box(modifier = Modifier.weight(1f)) {
            content()
        }
        if (trailing != null) {
            Spacer(modifier = Modifier.width(16.dp))
            trailing()
        }
    }
}

@Preview
@Composable
fun ListItemSwitchPreview(){
    ProjectTheme {
        Surface {
            ListItem(
                content = {
                    Text(
                        text = "Светлая темная авто",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                trailing = {
                    Switch(
                        checked = false,
                        onCheckedChange = {  }
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun ListItemPreview(){
    ProjectTheme {
        Surface {
            ListItem(
                content = {
                    Text(
                        text = "О приложении",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                trailing = {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null
                    )
                },
                onClick = { /* TODO: обработка нажатия */ }
            )
        }
    }
}