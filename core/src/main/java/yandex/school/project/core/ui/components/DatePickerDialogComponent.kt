package yandex.school.project.core.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import yandex.school.project.core.domain.entities.Category
import yandex.school.project.core.utils.formatDate
import yandex.school.project.core.utils.formatTime
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

/**
 * Компонент для отображения DatePickerDialog.
 */
@Composable
fun DatePickerDialogComponent(
    show: Boolean,
    initialDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    if (show) {
        val context = LocalContext.current
        val calendar = Calendar.getInstance()
        val year = initialDate?.year ?: calendar.get(Calendar.YEAR)
        val month = initialDate?.monthValue?.minus(1) ?: calendar.get(Calendar.MONTH)
        val day = initialDate?.dayOfMonth ?: calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(
            context,
            { _, y, m, d ->
                onDateSelected(LocalDate.of(y, m + 1, d))
                onDismiss()
            },
            year, month, day
        ).apply { setOnDismissListener { onDismiss() } }.show()
    }
}

@Composable
fun DateSelectorListItem(
    date: String,
    onDateChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val formattedDate = formatDate(date)
    val calendar = remember { Calendar.getInstance() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(56.dp)
            .clickable {
                val parsed = try { OffsetDateTime.parse(date) } catch (e: Exception) { null }
                val year = parsed?.year ?: calendar.get(Calendar.YEAR)
                val month = parsed?.monthValue?.minus(1) ?: calendar.get(Calendar.MONTH)
                val day = parsed?.dayOfMonth ?: calendar.get(Calendar.DAY_OF_MONTH)
                DatePickerDialog(
                    context,
                    { _, y, m, d ->
                        val newDate = OffsetDateTime.now()
                            .withYear(y)
                            .withMonth(m + 1)
                            .withDayOfMonth(d)
                        onDateChange(newDate.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                    },
                    year, month, day
                ).show()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Дата",
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = formattedDate,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
    HorizontalDivider()
}

@Composable
fun TimeSelectorListItem(
    date: String,
    onTimeChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val formattedTime = formatTime(date)
    val calendar = remember { Calendar.getInstance() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(56.dp)
            .clickable {
                val parsed = try { OffsetDateTime.parse(date) } catch (e: Exception) { null }
                val hour = parsed?.hour ?: calendar.get(Calendar.HOUR_OF_DAY)
                val minute = parsed?.minute ?: calendar.get(Calendar.MINUTE)
                TimePickerDialog(
                    context,
                    { _, h, m ->
                        val newDate = (parsed ?: OffsetDateTime.now())
                            .withHour(h)
                            .withMinute(m)
                        onTimeChange(newDate.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                    },
                    hour, minute, true
                ).show()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Время",
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = formattedTime,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
    HorizontalDivider()
}

@Composable
fun EditArgsText(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title
        )
        Spacer(Modifier.width(16.dp))
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .height(56.dp)
                .weight(1f),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            singleLine = true,
            shape = RectangleShape,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                errorContainerColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.End
            ),
        )
    }
    HorizontalDivider()
}

@Composable
fun CategorySelectorListItem(
    categories: List<Category>,
    selectedCategoryId: Int,
    onCategorySelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedCategory = categories.find { it.id == selectedCategoryId }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(56.dp)
            .clickable { expanded = true },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Статья",
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(16.dp))
        Box {
            Text(
                text = selectedCategory?.name ?: "",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { expanded = true }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .heightIn(max = 300.dp) // Ограничение по высоте
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            onCategorySelected(category.id)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
    HorizontalDivider()
}

@Composable
fun CommentTextListItem(
    modifier: Modifier = Modifier,
    comment: String,
    onCommentChange: (String) -> Unit,
    maxLength: Int = 40
) {
    val focusManager = LocalFocusManager.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Комментарий",
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(16.dp))
        OutlinedTextField(
            value = comment,
            onValueChange = {
                if (it.length <= maxLength) onCommentChange(it)
            },
            modifier = Modifier
                .height(56.dp)
                .weight(1.5f),
            singleLine = true,
            shape = RectangleShape,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                errorContainerColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.End
            ),
            placeholder = { Text("Комментарий") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            )
        )
    }
    HorizontalDivider()
}