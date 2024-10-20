package com.task.management.workflow.task.presentation


import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.task.management.workflow.task.domain.Task
import java.util.Calendar

@Composable
fun TaskCreationScreen(viewModel: TaskListViewModel, navController: NavController){
    val state = viewModel.state.value
    val name = viewModel.name.value
    val description = viewModel.description.value
    val dueDate by viewModel.dueDate
    val userId = viewModel.userId.value
    val projectId = viewModel.projectId.value
    val taskId = viewModel.taskId.value
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                value = name,
                onValueChange = {
                    viewModel.onNameChanged(it)
                },
                label = { Text("Name") }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                value = description,
                onValueChange = {
                    viewModel.onDescriptionChanged(it)
                },
                label = { Text("Description") }
            )

            Button(
                onClick = {
                    showDatePicker = true
                }
            ) {
                Text(text = "Due Date")
            }

            dueDate?.let {
                Text(text = "Selected Due Date: $it")
            }

            if (showDatePicker) {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                DatePickerDialog(
                    LocalContext.current,
                    { _, yearSelected, monthSelected, dayOfMonthSelected ->
                        val selectedDate = "$yearSelected-${monthSelected + 1}-$dayOfMonthSelected"
                        viewModel.onDueDateChanged(selectedDate)
                        showDatePicker = false
                    },
                    year, month, day
                ).show()
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                value = viewModel.userId.value.toString(),
                onValueChange = {
                    viewModel.onUserIdChanged(it)
                },
                label = { Text(text = "User ID") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                value = viewModel.projectId.value.toString(),
                onValueChange = {
                    viewModel.onProjectIdChanged(it)
                },
                label = { Text(text = "Project ID") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(
                onClick = {
                    val newTask = Task(name,description,dueDate,userId,projectId)
                }
            ) {
                Text("Create Task")
            }
        }
    }
}