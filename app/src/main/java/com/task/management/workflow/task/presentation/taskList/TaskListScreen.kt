package com.task.management.workflow.task.presentation.taskList


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.task.management.workflow.task.domain.Task
import com.task.management.workflow.task.presentation.dialogs.TaskDeleteDialog
import com.task.management.workflow.task.presentation.dialogs.TaskEditDialog
import com.task.management.workflow.task.presentation.taskFilter.TaskFilterOptions
import com.task.management.workflow.task.presentation.taskFilter.TaskFilterSection


@Composable
fun TaskListScreen(viewModel: TaskListViewModel, navController: NavController) {
    val state = viewModel.state.value
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedTask: Task? by remember { mutableStateOf(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var taskToDelete by remember { mutableStateOf<Task?>(null) }

    val filterOptions = TaskFilterOptions(
        projectId = viewModel.projectId.value.toString(),
        userId = viewModel.userId.value.toString(),
        status = viewModel.statusItem.value,
        onlyShowUser = viewModel.onlyShowUser.value
    )

    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TaskFilterSection(
                filterOptions = filterOptions,
                statusList = listOf("ALL", "NEW", "PENDING", "COMPLETED", "OVERDUE", "COMPLETED_OVERDUE"),
                onFilterOptionsChanged = { newFilterOptions ->
                    viewModel.onProjectIdChanged(newFilterOptions.projectId)
                    viewModel.onUserIdChanged(newFilterOptions.userId)
                    viewModel.onStatusItemChanged(newFilterOptions.status)
                    viewModel.onOnlyShowUserChanged(newFilterOptions.onlyShowUser)
                },
                onSearchClicked = { viewModel.searchTasks() }
            )

            if (state.isLoading) CircularProgressIndicator()
            if (state.error.isNotEmpty()) Text(state.error)

            state.data?.let { taskList ->
                TaskList(
                    tasks = taskList,
                    onTaskSelected = {
                        selectedTask = it
                        showEditDialog = true
                    },
                    onDeleteClicked = {
                        taskToDelete = it
                        showDeleteDialog = true
                    }
                )
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
                    onDismiss = { showDeleteDialog = false }
                )
            }
        }
    }
}
