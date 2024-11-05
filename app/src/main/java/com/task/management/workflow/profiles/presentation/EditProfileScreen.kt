package com.task.management.workflow.profiles.presentation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EditUserScreen(
    onConfirm: (String, String, String) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }

    var isNameValid by remember { mutableStateOf(true) }
    var isPhoneValid by remember { mutableStateOf(true) }
    var isCompanyValid by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = name,
            onValueChange = {
                name = it
                isNameValid = it.length > 3
            },
            label = { Text("Nombre") },
            isError = !isNameValid,
            modifier = Modifier.fillMaxWidth()
        )
        if (!isNameValid) {
            Text(
                text = "El nombre debe tener más de 3 caracteres",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = phone,
            onValueChange = {
                phone = it
                isPhoneValid = it.length > 5
            },
            label = { Text("Celular") },
            isError = !isPhoneValid,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )
        if (!isPhoneValid) {
            Text(
                text = "El número debe tener más de 5 caracteres",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = company,
            onValueChange = {
                company = it
                isCompanyValid = it.length > 3
            },
            label = { Text("Compañía") },
            isError = !isCompanyValid,
            modifier = Modifier.fillMaxWidth()
        )
        if (!isCompanyValid) {
            Text(
                text = "La compañía debe tener más de 3 caracteres",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Button(
                onClick = {
                    if (isNameValid && isPhoneValid && isCompanyValid) {
                        onConfirm(name, phone, company)
                    }
                },
                enabled = isNameValid && isPhoneValid && isCompanyValid,
                modifier = Modifier.weight(1f)
            ) {
                Text("Confirm")
            }

            Spacer(modifier = Modifier.width(16.dp))

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }
        }
    }
}


@Preview
@Composable
fun EditUserScreenPreview() {
    EditUserScreen(onConfirm = { _, _ , _-> }, onCancel = { })
}