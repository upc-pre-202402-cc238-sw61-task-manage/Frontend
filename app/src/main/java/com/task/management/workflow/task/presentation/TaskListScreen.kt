package com.task.management.workflow.task.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.task.management.workflow.task.domain.Task


@Composable
fun TaskListScreen(viewModel: TaskListViewModel, navController: NavController){
    val state = viewModel.state.value

    LaunchedEffect(Unit) {
        viewModel.getTasks()
    }

    Scaffold { paddingValues ->
        Column (modifier = Modifier.padding(paddingValues)) {
            if (state.isLoading){
                CircularProgressIndicator()
            }
            if (state.error.isNotEmpty()){
                Text(state.error)
            }

            state.data?.let { taskList: List<Task> ->
                LazyColumn {
                    items(taskList) { task ->
                        Card (modifier = Modifier.padding(4.dp)) {
                            Text(
                                modifier = Modifier.padding(4.dp),
                                text = task.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text(
                                modifier = Modifier.padding(4.dp),
                                text = task.description
                            )
                            Text(
                                modifier = Modifier.padding(4.dp),
                                text = task.dueDate
                            )
                        }
                    }
                }

            }
        }
    }
}