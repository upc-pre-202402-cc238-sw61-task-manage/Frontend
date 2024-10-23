package com.task.management.workflow.task.presentation

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.task.management.workflow.task.domain.Task
import com.task.management.workflow.task.domain.TaskStatus
import com.task.management.workflow.ui.theme.FailureColor
import com.task.management.workflow.ui.theme.SuccessColor
import java.util.Calendar

@Composable
fun TaskEditDialog(
    task: Task,
    onDismiss: () -> Unit,
    onSaveChanges: (Task) -> Unit
) {
    var name by remember { mutableStateOf(task.name) }
    var description by remember { mutableStateOf(task.description) }
    var dueDate by remember { mutableStateOf(task.dueDate) }
    var status by remember { mutableStateOf(task.status) }
    var expanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Edit Task",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )

                Button(
                    onClick = { showDatePicker = true }
                ) {
                    Text(text = "Due Date")
                }
                Text(text = "Due Date: $dueDate")

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

                Text(
                    text="Selected status: $status",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(4.dp)
                )

                Box{
                    Button(onClick = { expanded = true }) {
                        Text("Task Status")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { /* Handle dismiss */ }
                    ) {
                        TaskStatus.entries.forEach { taskStatus ->
                            if (taskStatus != TaskStatus.NEW && taskStatus != TaskStatus.OVERDUE) {
                                DropdownMenuItem(
                                    onClick = { status = taskStatus; expanded = !expanded },
                                    text = { Text(text = taskStatus.name) }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                val updatedTask = task.copy(
                    name = name,
                    description = description,
                    dueDate = dueDate,
                    status = status
                )
                onSaveChanges(updatedTask)
                },
                colors = ButtonColors(
                    SuccessColor,
                    Color.White,
                    Color.Gray,
                    Color.Gray
                )
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonColors(
                    FailureColor,
                    Color.White,
                    Color.Gray,
                    Color.Gray
                )
            ) {
                Text("Cancel")
            }
        }
    )
}
