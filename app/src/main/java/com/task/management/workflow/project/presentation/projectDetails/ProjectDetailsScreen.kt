package com.task.management.workflow.project.presentation.projectDetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.task.management.workflow.projectUser.presentation.ProjectUserViewModel
import com.task.management.workflow.projectUser.presentation.ProjectUsersList
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
    val taskList by projectViewModel.tasks
    var showEditDialog by remember { mutableStateOf(false) }

    LaunchedEffect(projectId) {
        projectViewModel.getProjectById(projectId)
        projectViewModel.loadProjectTasks(projectId)
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
                        .padding(vertical = 12.dp),
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
                        Text(
                            text = "Leader: ${project!!.leader}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                Button(
                    onClick = { showEditDialog = true },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(top = 16.dp)
                        .height(48.dp)
                        .align(Alignment.End),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonColors(
                        SlateBlueColor,
                        Color.White,
                        Color.Gray,
                        Color.Gray
                    )
                ) {
                    Text(
                        text = "Edit Project",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }

                Text(
                    text = "Project Tasks",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .align(Alignment.Start)
                )

                if (taskList.isNotEmpty()) {
                    TaskList(
                        tasks = taskList,
                        onTaskSelected = {}, //TODO
                        onDeleteClicked = {} //TODO
                    )
                } else {
                    Text(
                        text = "No tasks available for this project.",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Text(
                    text = "Project Members",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .align(Alignment.Start)
                )

                Box(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ){
                    ProjectUsersList(projectId = projectId, projectUserViewModel = projectUserViewModel)
                }

                if (showEditDialog) {
                    ProjectEditionDialog(
                        project = project!!,
                        onConfirm = { newTitle, newDescription ->
                            projectViewModel.updateProject(
                                project!!.copy(
                                    title = newTitle,
                                    description = newDescription
                                )
                            )
                            showEditDialog = false
                        },
                        onDismissRequest = { showEditDialog = false }
                    )
                }
            } else {
                Text(
                    text = "Project not found",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
