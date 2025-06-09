package yandex.school.project.ui.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import yandex.school.project.ui.theme.ProjectTheme

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    leadingIcon: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {},
    extraContent: @Composable () -> Unit = {},
    endingIcon: @Composable () -> Unit = {},
    onClick: () -> Unit = {}
){

}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun ListItemPreview(){
    ProjectTheme {
        Surface {
            ListItem()
        }
    }
}