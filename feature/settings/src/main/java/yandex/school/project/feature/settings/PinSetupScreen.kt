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
fun PinSetupScreen(
    onPinSet: (String) -> Unit,
    onCancel: () -> Unit
) {
    var pin1 by remember { mutableStateOf("") }
    var pin2 by remember { mutableStateOf("") }
    var step by remember { mutableStateOf(1) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (step == 1) "Введите новый PIN-код" else "Повторите PIN-код",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = if (step == 1) pin1 else pin2,
            onValueChange = {
                if (it.length <= 4 && it.all { c -> c.isDigit() }) {
                    if (step == 1) pin1 = it else pin2 = it
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("PIN-код") },
            singleLine = true
        )
        if (error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = error!!, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row {
            Button(onClick = onCancel, modifier = Modifier.weight(1f)) {
                Text("Отмена")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    if (step == 1) {
                        if (pin1.length == 4) {
                            step = 2
                            error = null
                        } else {
                            error = "Введите 4 цифры"
                        }
                    } else {
                        if (pin2.length == 4) {
                            if (pin1 == pin2) {
                                onPinSet(pin1)
                            } else {
                                error = "PIN-коды не совпадают"
                                pin2 = ""
                            }
                        } else {
                            error = "Введите 4 цифры"
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (step == 1) "Далее" else "Сохранить")
            }
        }
    }
} 