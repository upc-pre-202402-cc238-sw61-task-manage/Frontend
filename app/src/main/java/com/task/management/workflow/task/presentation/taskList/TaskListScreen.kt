package com.task.management.workflow.task.presentation.taskList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.task.management.workflow.task.domain.Task
import com.task.management.workflow.task.presentation.taskCreation.TaskCreationViewModel

@Composable
fun TaskListScreen(viewModel: TaskCreationViewModel, navController: NavController){
    val state = viewModel.state.value

    Scaffold { paddingValues: PaddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Button(
                onClick = {
                    viewModel.getAllRemoteTasks()
                }
            ){
                Text("Show all Tasks")
            }
            if (state.isLoading) {
                CircularProgressIndicator()
            }
            if(state.error.isNotEmpty()){
                Text(state.error)
            }
            state.tasks?.let { tasks ->
                LazyColumn {
                    items(tasks) { task ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            TaskItem(task)
                        }
                    }
                }
            } ?: run {
                Text("No tasks available")
            }
        }
    }
}

@Composable
fun TaskItem(task: Task) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column (modifier = Modifier
            .padding(4.dp)
            .weight(1f)
        ) {
            Text(task.name)
            Text(task.description)
            Text("User ID: ${task.userId}")
            Text("Project ID: ${task.projectId}")
        }
    }
}

