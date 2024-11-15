package com.task.management.workflow.project.presentation.projectList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.task.management.workflow.common.constants.NavigationConstants
import com.task.management.workflow.common.session.UserSession
import com.task.management.workflow.project.domain.Project
import com.task.management.workflow.project.presentation.ProjectViewModel
import com.task.management.workflow.project.presentation.dialogs.ProjectCreationDialog
import com.task.management.workflow.projectUser.presentation.ProjectUserViewModel
import com.task.management.workflow.ui.theme.DeepIndigoColor
import com.task.management.workflow.ui.theme.SlateBlueColor

@Composable
fun ProjectListScreen(
    projectViewModel: ProjectViewModel,
    projectUserViewModel: ProjectUserViewModel,
    navController: NavController) {
    val projectsState = projectUserViewModel.userProjects.value
    var showDialog by remember { mutableStateOf(false) }
    val userId = UserSession.userId.collectAsState().value
    LaunchedEffect(Unit) {
        if(userId != null){
            projectUserViewModel.getProjectsByUserId(userId)
        }
    }
    LaunchedEffect(projectUserViewModel.isProjectListUpdated.value) {
        if (projectUserViewModel.isProjectListUpdated.value) {
            projectUserViewModel.getProjectsByUserId(userId!!)
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "My Projects",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DeepIndigoColor,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            Button(
                onClick = {
                    showDialog = true
                },
                modifier = Modifier
                    .padding(6.dp)
                    .align(Alignment.End),
                colors = ButtonColors(
                    SlateBlueColor,
                    Color.White,
                    Color.Gray,
                    Color.Gray
                )
            ) {
                Text(text = "Create a new project")
            }
            if (projectsState.isLoading) {
                CircularProgressIndicator()
            } else {
                projectsState.data?.let { projectList: List<Project> ->
                    LazyColumn {
                        items(projectList) { project ->
                            ProjectCard(
                                project = project,
                                onClick = { navController.navigate("${NavigationConstants.PROJECT_DETAILS_PATH}/${project.projectId}") }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        ProjectCreationDialog(
            onCreateProject = { name, description, leader ->
                showDialog = false
                projectViewModel.createProject(name, description, leader) { projectId ->
                    if (projectId != null && userId != null) {
                        projectUserViewModel.addUserToProject(projectId, userId)
                        projectUserViewModel.getProjectsByUserId(userId)
                    }
                }
            },
            onDismissRequest = { showDialog = false }
        )
    }
}