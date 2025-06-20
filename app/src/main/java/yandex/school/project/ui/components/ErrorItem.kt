package yandex.school.project.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ErrorItem(
    modifier: Modifier = Modifier,
    errorMessage: String,
    onRetryClick: ()->Unit
){
    Box(modifier = modifier.fillMaxSize().padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = errorMessage)
            androidx.compose.material3.Button(
                onClick = onRetryClick,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Повторить попытку")
            }
        }
    }
}