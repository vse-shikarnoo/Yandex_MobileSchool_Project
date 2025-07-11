package yandex.school.project.core.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

// Состояние для TopBar
data class TopBarState(
    val navigationIcon: ImageVector? = null,
    val navigationIconAction: () -> Unit = {},
    val title: String,
    val actionIcon: ImageVector? = null,
    val actionIconAction: () -> Unit = {},
    val isFAB: Boolean = false,
    val actionFAB: () -> Unit = {},
    val isEditingTitle: Boolean = false,
    val titleInput: TextFieldValue = TextFieldValue(""),
    val onTitleInputChange: ((TextFieldValue) -> Unit)? = null,
    val onTitleEditDone: (() -> Unit)? = null
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
            Crossfade(targetState = state.isEditingTitle) { editing ->
                if (editing && state.onTitleInputChange != null && state.onTitleEditDone != null) {
                    OutlinedTextField(
                        value = state.titleInput,
                        onValueChange = state.onTitleInputChange,
                        singleLine = true,
                        textStyle = MaterialTheme.typography.titleLarge,
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f)
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                            onDone = { state.onTitleEditDone.invoke() }
                        ),
                        modifier = Modifier
                            .padding(vertical = 4.dp),
                        trailingIcon = {
                            if (state.titleInput.text.isNotEmpty()) {
                                IconButton(onClick = {
                                    state.onTitleInputChange.invoke(
                                        TextFieldValue("")
                                    )
                                }) {
                                    Icon(Icons.Default.Close, contentDescription = "Очистить")
                                }
                            }
                        }
                    )
                } else {
                    Text(
                        text = state.title,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        },
        actions = {
            if (state.isEditingTitle && state.onTitleEditDone != null) {
                IconButton(onClick = state.onTitleEditDone) {
                    Icon(Icons.Default.Check, contentDescription = "Сохранить")
                }
            } else if (state.actionIcon != null) {
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