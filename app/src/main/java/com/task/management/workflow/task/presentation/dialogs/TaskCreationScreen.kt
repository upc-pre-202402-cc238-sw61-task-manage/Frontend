package com.task.management.workflow.task.presentation.dialogs


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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.task.management.workflow.task.presentation.taskList.TaskListViewModel
import java.util.Calendar

@Composable
fun TaskCreationScreen(viewModel: TaskListViewModel, navController: NavController){
    val state = viewModel.state.value
    val name = viewModel.name.value
    val description = viewModel.description.value
    val dueDate by viewModel.dueDate
    val userId = viewModel.userId.value
    val projectId = viewModel.projectId.value
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold { paddingValues ->
        Column (
            modifier = Modifier
                .padding(paddingValues)
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create a new Task",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 20.dp)
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                value = name,
                onValueChange = {
                    viewModel.onNameChanged(it)
                },
                label = { (Text("Name"))}
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                value = description,
                onValueChange = {
                    viewModel.onDescriptionChanged(it)
                },
                label = { Text("Description") }
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                value = userId.toString(),
                onValueChange = {
                    viewModel.onUserIdChanged(it)
                },
                label = { Text(text = "User ID") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                value = projectId.toString(),
                onValueChange = {
                    viewModel.onProjectIdChanged(it)
                },
                label = { Text(text = "Project ID") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Button(
                onClick = { showDatePicker = true }
            ) {
                Text(text = "Due Date")
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
                        viewModel.onDueDateChanged(selectedDate)
                        showDatePicker = false
                    },
                    year, month, day
                ).show()
            }

            Button(
                modifier = Modifier.padding(10.dp),
                onClick = {
                    viewModel.createTask()
                }
            ) {
                Text(text = "Create Task")
            }
        }
    }
}