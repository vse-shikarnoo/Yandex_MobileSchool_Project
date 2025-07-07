package yandex.school.project.presentation.components

import android.app.DatePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate
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