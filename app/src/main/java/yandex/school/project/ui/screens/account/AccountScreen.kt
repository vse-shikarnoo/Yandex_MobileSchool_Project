package yandex.school.project.ui.screens.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import yandex.school.project.ui.components.ListItem
import yandex.school.project.ui.theme.ProjectTheme

@Composable
fun AccountScreen(
    viewModel: AccountViewModel = viewModel()
) {
    val accountState = viewModel.accountState

    Column(
        modifier = Modifier
            .fillMaxWidth()
            //.background(color = MaterialTheme.colorScheme.secondary)
    ) {
        // Баланс
        ListItem(
            modifier = Modifier.height(56.dp),
            leadingIcon = "💰", // Можно заменить на иконку, если появится
            contentTitle = "Баланс",
            contentSecond = {
                Text(
                    text = accountState.balance,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            backgroundColor = MaterialTheme.colorScheme.secondary,
            iconBackgroundColor = MaterialTheme.colorScheme.surface
        )
        HorizontalDivider()
        // Валюта
        ListItem(
            modifier = Modifier.height(56.dp),
            contentTitle = "Валюта",
            contentSecond = {
                Text(
                    text = accountState.currency,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            backgroundColor = MaterialTheme.colorScheme.secondary
        )
    }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun AccountScreenPreview() {
    ProjectTheme {
        Surface {
            AccountScreen()
        }
    }
}