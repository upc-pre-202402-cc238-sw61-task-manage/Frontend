package com.task.management.workflow.project.presentation.projectDetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.SwapHorizontalCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.task.management.workflow.project.presentation.ProjectViewModel
import com.task.management.workflow.project.presentation.dialogs.ProjectDeleteDialog
import com.task.management.workflow.project.presentation.dialogs.ProjectEditionDialog
import com.task.management.workflow.projectUser.presentation.ProjectUserViewModel
import com.task.management.workflow.projectUser.presentation.ProjectUsersList
//import com.task.management.workflow.projectUser.presentation.dialogs.ProjectUserCreationDialog TODO
import com.task.management.workflow.task.domain.Task
import com.task.management.workflow.task.presentation.dialogs.TaskCreationDialog
import com.task.management.workflow.task.presentation.dialogs.TaskDeleteDialog
import com.task.management.workflow.task.presentation.dialogs.TaskEditDialog
import com.task.management.workflow.task.presentation.taskFilter.TaskFilterOptions
import com.task.management.workflow.task.presentation.taskFilter.TaskFilterSection
import com.task.management.workflow.task.presentation.taskList.TaskList
import com.task.management.workflow.ui.theme.DeepIndigoColor
import com.task.management.workflow.ui.theme.SlateBlueColor

@Composable
fun ProjectDetailScreen(
    projectViewModel: ProjectViewModel,
    projectUserViewModel: ProjectUserViewModel,
    navController: NavController,
    projectId: Long
) {
    val project by projectViewModel.selectedProject
    val taskList = projectViewModel.taskList.value
    var selectedTask: Task? by remember { mutableStateOf(null) }
    var showProjectEditDialog by remember { mutableStateOf(false) }
    var showTaskEditDialog by remember { mutableStateOf(false) }
    var showTasks by remember { mutableStateOf(true) }
    var showTaskCreationDialog by remember { mutableStateOf(false) }
    var taskToDelete by remember { mutableLongStateOf(0) }
    var showTaskDeleteDialog by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("") }
    var onlyShowUser by remember { mutableStateOf(false) }
    var showProjectDeleteDialog by remember { mutableStateOf(false) }
    var showUserAdditionDialog by remember { mutableStateOf(false) }


    val filterOptions = TaskFilterOptions(
        projectId = projectId.toString(),
        userId = "1", //TODO
        status = status,
        onlyShowUser = onlyShowUser
    )

    LaunchedEffect(projectId) {
        projectViewModel.getProjectById(projectId)
        projectViewModel.getProjectTasks(projectId)
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Project Details",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DeepIndigoColor,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            if (project != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .clickable { showProjectEditDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = project!!.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = project!!.description,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Leader: ${project!!.leader}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.weight(1f)
                            )

                            IconButton(
                                onClick = { showProjectDeleteDialog = true },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Project",
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Box(modifier = Modifier.width(160.dp)) {
                        Text(
                            text = if (showTasks) "Tasks" else "Members",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                        IconButton(
                            onClick = { showTasks = !showTasks },
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHorizontalCircle,
                                contentDescription = "Edit Project",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            if (showTasks) showTaskCreationDialog = true
                            else showUserAdditionDialog = true
                        },
                        colors = ButtonColors(
                            SlateBlueColor,
                            Color.White,
                            Color.Gray,
                            Color.Gray
                        )
                    ) {
                        Icon(
                            imageVector = if(showTasks) Icons.Default.Add else Icons.Default.PersonAdd,
                            contentDescription = "Create new object")
                    }
                }

                if (showTaskCreationDialog) {
                    TaskCreationDialog(
                        onConfirmRequest = { name, description, dueDate, userId ->
                            projectViewModel.createTask(name,description,dueDate,projectId, userId)
                            showTaskCreationDialog = false
                        },
                        onDismissRequest = { showTaskCreationDialog = false }
                    )
                }

                if(showTasks){
                    when {
                        taskList.isLoading -> CircularProgressIndicator()
                        taskList.error.isNotEmpty() -> Text(text = taskList.error, color = Color.Red)
                        taskList.data != null -> {
                            TaskFilterSection(
                                filterOptions = filterOptions,
                                statusList = listOf("ALL", "NEW", "PENDING", "COMPLETED", "OVERDUE", "COMPLETED_OVERDUE"),
                                onFilterOptionsChanged = { newFilterOptions ->
                                    status = newFilterOptions.status
                                    onlyShowUser = newFilterOptions.onlyShowUser
                                },
                                onSearchClicked = { projectViewModel.getTasks(projectId,status, onlyShowUser) }
                            )

                            TaskList(
                                tasks = taskList.data!!,
                                onTaskSelected = {
                                    selectedTask = it
                                    showTaskEditDialog = true
                                },
                                onDeleteClicked = {
                                    taskToDelete = it
                                    showTaskDeleteDialog = true
                                }
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ){
                        ProjectUsersList(projectId = projectId, projectUserViewModel = projectUserViewModel)
                    }
                }
            } else {
                Text(
                    text = "Project not found",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (showProjectEditDialog) {
                ProjectEditionDialog(
                    project = project!!,
                    onConfirm = { newTitle, newDescription ->
                        projectViewModel.updateProject(
                            project!!.copy(
                                title = newTitle,
                                description = newDescription
                            )
                        )
                        showProjectEditDialog = false
                    },
                    onDismissRequest = { showProjectEditDialog = false }
                )
            }

            if (showProjectDeleteDialog) {
                ProjectDeleteDialog(
                    onConfirm = {
                        projectViewModel.deleteProject(projectId)
                        showProjectDeleteDialog = false
                        navController.popBackStack()
                    },
                    onDismissRequest = { showProjectDeleteDialog = false }
                )
            }

            if (showTaskEditDialog && selectedTask != null) {
                TaskEditDialog(
                    task = selectedTask!!,
                    onDismiss = { showTaskEditDialog = false },
                    onConfirm = { updatedTask ->
                        projectViewModel.updateTask(updatedTask.copy(), projectId)
                        showTaskEditDialog = false
                    }
                )
            }

            if (showTaskDeleteDialog && taskToDelete != 0.toLong()) {
                TaskDeleteDialog(
                    onConfirm = {
                        projectViewModel.deleteTask(taskToDelete,projectId)
                        showTaskDeleteDialog = false
                    },
                    onDismiss = { showTaskDeleteDialog = false }
                )
            }

            /*
            if (showUserAdditionDialog) {
                //TODO

                ProjectUserCreationDialog(
                    projectId = projectId,
                    projectUserViewModel = projectUserViewModel,
                    onDismissRequest = { showUserAdditionDialog = false }
                )

            }
            */

        }
    }
}