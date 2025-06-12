package yandex.school.project.ui.screens.expenses

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ExpensesScreen(
    onBtnClick: ()->Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = "Expenses")
        Button(
            onClick = onBtnClick
        ) {
            Text(text = "ExpensesCreate")
        }
    }
}