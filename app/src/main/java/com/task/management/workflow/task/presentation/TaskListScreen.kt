package com.task.management.workflow.task.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.task.management.workflow.task.domain.Task
import com.task.management.workflow.task.domain.TaskStatus
import com.task.management.workflow.ui.theme.FailureColor
import com.task.management.workflow.ui.theme.HighlightColor
import com.task.management.workflow.ui.theme.RecentColor
import com.task.management.workflow.ui.theme.SuccessColor
import com.task.management.workflow.ui.theme.WarningColor


@Composable
fun TaskListScreen(viewModel: TaskListViewModel, navController: NavController){
    val state = viewModel.state.value

    val status = viewModel.statusItem.value
    var onlyShowUser = viewModel.onlyShowUser.value

    var showEditDialog by remember { mutableStateOf(false) }
    var selectedTask: Task? by remember { mutableStateOf(null) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var taskToDelete by remember { mutableStateOf<Task?>(null) }

    val statusList = listOf(
        "ALL",
        "NEW",
        "PENDING",
        "COMPLETED",
        "OVERDUE",
        "COMPLETED_OVERDUE"
    )

    Scaffold { paddingValues ->
        Column (modifier = Modifier.padding(paddingValues)) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 10.dp,
                        bottom = 10.dp
                    ),
                value = viewModel.projectId.value.toString(),
                onValueChange = {
                    viewModel.onProjectIdChanged(it)
                },
                label = { Text(text = "Project ID") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 10.dp,
                        bottom = 10.dp
                    ),
                value = viewModel.userId.value.toString(),
                onValueChange = {
                    viewModel.onUserIdChanged(it)
                },
                label = { Text(text = "User ID") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Row (
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Only show my tasks: ",
                    modifier = Modifier
                        .padding(end = 10.dp, top = 14.dp)
                )
                FilterChip(
                    modifier = Modifier.padding(4.dp),
                    selected = onlyShowUser,
                    label = {
                        Text("Show my tasks")
                    },
                    leadingIcon = {
                        if (onlyShowUser) {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    },
                    onClick = {
                        onlyShowUser = !onlyShowUser
                        viewModel.onOnlyShowUserChanged(onlyShowUser)
                    }
                )
            }

            LazyRow (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                items(statusList) { statusItem ->
                    FilterChip(
                        modifier = Modifier.padding(4.dp),
                        selected = status == statusItem,
                        label = {
                         Text(statusItem)
                        },
                        leadingIcon = {
                            if(status == statusItem) {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        },
                        onClick = {
                            viewModel.onStatusItemChanged(statusItem)
                        }
                    )
                }
            }

            Button(
                onClick = { viewModel.searchTasks() },
                colors = ButtonColors(
                    SuccessColor,
                    Color.White,
                    Color.Gray,
                    Color.Gray
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.5f)
            ) {
                Text(
                    text = "Search",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            if (state.isLoading){
                CircularProgressIndicator()
            }
            if (state.error.isNotEmpty()){
                Text(state.error)
            }
            state.data?.let { taskList: List<Task> ->
                LazyColumn {
                    items(taskList) { task ->
                        Card (modifier = Modifier
                            .padding(
                                start = 20.dp,
                                end = 20.dp,
                                top = 10.dp,
                                bottom = 10.dp
                            )
                            .fillMaxWidth()
                            .clickable {
                                selectedTask = task
                                showEditDialog = true
                            }
                        ) {
                            Column (modifier = Modifier
                                .padding(
                                    top = 10.dp,
                                    end = 16.dp
                                )
                            ) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                    ,
                                    text = task.dueDate,
                                    textAlign = TextAlign.Right,
                                )

                                Box(
                                    modifier = Modifier
                                        .padding(
                                            bottom = 4.dp
                                        )
                                        .height(10.dp)
                                        .fillMaxWidth(0.4f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .align(Alignment.End)
                                        .background(
                                            when (task.status) {
                                                TaskStatus.NEW -> RecentColor
                                                TaskStatus.PENDING -> WarningColor
                                                TaskStatus.COMPLETED -> SuccessColor
                                                TaskStatus.OVERDUE -> FailureColor
                                                TaskStatus.COMPLETED_OVERDUE -> HighlightColor
                                            }
                                        )
                                )
                            }

                            Column(modifier = Modifier
                                .padding(
                                    start = 16.dp,
                                    bottom = 16.dp
                                )
                            ) {
                                Text(
                                    text = task.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .padding(bottom = 6.dp)
                                )
                                Text(task.description)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    IconButton(
                                        onClick = {
                                            taskToDelete = task
                                            showDeleteDialog = true
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete Task",
                                            tint = Color.DarkGray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (showEditDialog && selectedTask != null) {
                TaskEditDialog(
                    task = selectedTask!!,
                    onDismiss = { showEditDialog = false },
                    onSaveChanges = { updatedTask ->
                        viewModel.updateTask(updatedTask)
                        showEditDialog = false
                    }
                )
            }
            if (showDeleteDialog && taskToDelete != null) {
                TaskDeleteDialog(
                    onConfirm = {
                        viewModel.deleteTask(taskToDelete!!)
                        showDeleteDialog = false
                    },
                    onDismiss = {
                        showDeleteDialog = false
                    }
                )
            }
        }
    }
}