package com.task.management.workflow.task.presentation.dialogs

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

@Composable
fun TaskCreationDialog(
    onDismissRequest: () -> Unit,
    onConfirmRequest: (String, String, String, Long) -> Unit
) {
    var name  by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var userId by remember { mutableLongStateOf(1) }
    var showDatePicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "Create a new Task", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    value = userId.toString(),
                    onValueChange = { userId = it.toLong() },
                    label = { Text(text = "User ID") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Button(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(text = "Select Due Date")
                }
                Text(text = "Selected Due Date: $dueDate")

                if (showDatePicker) {
                    val calendar = Calendar.getInstance()
                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH)
                    val day = calendar.get(Calendar.DAY_OF_MONTH)

                    DatePickerDialog(
                        LocalContext.current,
                        { _, yearSelected, monthSelected, dayOfMonthSelected ->
                            val selectedDate = "$yearSelected-${monthSelected + 1}-$dayOfMonthSelected"
                            dueDate = selectedDate
                            showDatePicker = false
                        },
                        year, month, day
                    ).show()
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirmRequest(name,description,dueDate,userId)
                onDismissRequest()
            }) {
                Text("Create Task")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}