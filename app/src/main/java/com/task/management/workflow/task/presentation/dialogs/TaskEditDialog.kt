package com.task.management.workflow.task.presentation.dialogs

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.task.management.workflow.task.domain.Task
import com.task.management.workflow.task.domain.TaskStatus
import com.task.management.workflow.user.presentation.UserViewModel
import java.util.Calendar

@Composable
fun TaskEditDialog(
    task: Task,
    userViewModel: UserViewModel,
    onDismiss: () -> Unit,
    onConfirm: (Task) -> Unit
) {
    var name by remember { mutableStateOf(task.name) }
    var description by remember { mutableStateOf(task.description) }
    var dueDate by remember { mutableStateOf(task.dueDate) }
    var status by remember { mutableStateOf(task.status) }
    var userId by remember { mutableStateOf(task.userId) }
    var showDatePicker by remember { mutableStateOf(false) }
    var expandedUser by remember { mutableStateOf(false) }
    var expandedStatus by remember { mutableStateOf(false) }

    // Fetch all users
    val users = userViewModel.usersState.value

    LaunchedEffect(Unit) {
        userViewModel.getAllUsers()
    }

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

                // Date Picker
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

                // User dropdown
                Box {
                    Button(onClick = {
                        expandedUser = true
                        expandedStatus = false // Close other menu if open
                    }) {
                        Text(text = users.data?.find { it.id == userId }?.username ?: "Select User")
                    }
                    DropdownMenu(
                        expanded = expandedUser,
                        onDismissRequest = { expandedUser = false }
                    ) {
                        users.data?.forEach { user ->
                            DropdownMenuItem(
                                onClick = {
                                    userId = user.id
                                    expandedUser = false
                                },
                                text = { Text(text = user.username) }
                            )
                        }
                    }
                }

                // Status dropdown
                Text(
                    text = "Selected status: $status",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(4.dp)
                )

                Box {
                    Button(onClick = {
                        expandedStatus = true
                        expandedUser = false // Close other menu if open
                    }) {
                        Text("Task Status")
                    }
                    DropdownMenu(
                        expanded = expandedStatus,
                        onDismissRequest = { expandedStatus = false }
                    ) {
                        TaskStatus.entries.forEach { taskStatus ->
                            if (taskStatus != TaskStatus.NEW && taskStatus != TaskStatus.OVERDUE) {
                                DropdownMenuItem(
                                    onClick = {
                                        status = taskStatus
                                        expandedStatus = false
                                    },
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
                        status = status,
                        userId = userId
                    )
                    onConfirm(updatedTask)
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}



