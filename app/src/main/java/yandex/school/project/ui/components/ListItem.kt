package yandex.school.project.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import yandex.school.project.ui.theme.ProjectTheme
import yandex.school.project.ui.utils.isEmoji

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    leadingIcon: String? = null,
    contentTitle: String,
    contentSecond: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    backgroundColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surface,
    iconBackgroundColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.secondary
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .background(backgroundColor)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(iconBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = leadingIcon,
                    fontSize = if (isEmoji(leadingIcon)) 18.sp else 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
        Box(modifier = Modifier.weight(1f)) {
            Text(
                text = contentTitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        if (contentSecond != null){
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                contentSecond()
            }
        }
        if (trailing != null) {
            Spacer(modifier = Modifier.width(16.dp))
            trailing()
        }
    }
}


//🏡👗🐶🍭🏋️💊
@Preview
@Composable
fun ListItemIconPreview() {
    ProjectTheme {
        Surface {
            val leadingIcon = "\uD83D\uDC57"
            ListItem(
                leadingIcon = leadingIcon,
                contentTitle = "Аренда квартиры",
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                iconBackgroundColor = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Preview
@Composable
fun ListItemStringPreview() {
    ProjectTheme {
        Surface {
            ListItem(
                leadingIcon = "АК",
                contentTitle = "Аренда квартиры",
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                iconBackgroundColor = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Preview
@Composable
fun ListItemSwitchPreview() {
    ProjectTheme {
        Surface {
            ListItem(
                contentTitle = "Светлая темная авто",
                trailing = {
                    Switch(
                        checked = false,
                        onCheckedChange = { }
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun ListItemPreview() {
    ProjectTheme {
        Surface {
            ListItem(
                contentTitle = "О приложении",
                contentSecond = {
                    Text(
                        text = "500 000",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                trailing = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null
                    )
                },
                onClick = { /* TODO: обработка нажатия */ }
            )
        }
    }
}