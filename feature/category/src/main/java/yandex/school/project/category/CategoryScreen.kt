package yandex.school.project.category

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import yandex.school.project.category.di.CategoryComponent
import yandex.school.project.category.di.LocalCategoryViewModelFactory

@Composable
fun ProvidedCategorytScreen(
    categoryComponent: CategoryComponent
) {
    val viewModelFactory = remember { categoryComponent.viewModelFactory() }
    CompositionLocalProvider(LocalCategoryViewModelFactory provides viewModelFactory) {
        CategoryScreen()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CategoryScreen() {
    val factory = LocalCategoryViewModelFactory.current
    val viewModel: CategoryViewModel = viewModel(factory = factory)
    var search by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    // Используем CoroutineManager для автоматического управления корутинами
    val coroutineManager = yandex.school.project.core.utils.rememberCoroutineManager(viewModel)

    LaunchedEffect(Unit) {
        coroutineManager.launchWithCancelPrevious {
            viewModel.loadCategoriesWithRetry()
        }
    }

    yandex.school.project.core.ui.components.ResultScreen(
        result = uiState,
        onRetry = { viewModel.loadCategoriesWithRetry() },
        coroutineManager = coroutineManager
    ) { categories ->
        val filtered = categories.filter { it.name.contains(search, ignoreCase = true) }
        HorizontalDivider()
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            stickyHeader {
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    placeholder = { Text("Найти статью") },
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    },
                    singleLine = true,
                    shape = RectangleShape,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        errorContainerColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                )
                HorizontalDivider()
            }
            items(filtered) { category ->
                yandex.school.project.core.ui.components.ListItem(
                    modifier = Modifier.height(70.dp),
                    leadingIcon = category.icon ?: "📁",
                    contentTitle = category.name
                )
                HorizontalDivider()
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun CategoryScreenPreview() {
    yandex.school.project.core.theme.ProjectTheme {
        Surface {
            CategoryScreen()
        }
    }
}