package yandex.school.project.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun PinEnterScreen(
    onPinEntered: (String) -> Unit,
    onCancel: (() -> Unit)? = null,
    errorMessage: String? = null
) {
    var pin by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Введите PIN-код",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = pin,
            onValueChange = {
                if (it.length <= 4 && it.all { c -> c.isDigit() }) {
                    pin = it
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("PIN-код") },
            singleLine = true
        )
        if (errorMessage != null || localError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage ?: localError!!, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row {
            if (onCancel != null) {
                Button(onClick = { onCancel() }, modifier = Modifier.weight(1f)) {
                    Text("Отмена")
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            Button(
                onClick = {
                    if (pin.length == 4) {
                        localError = null
                        onPinEntered(pin)
                    } else {
                        localError = "Введите 4 цифры"
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Готово")
            }
        }
    }
} 