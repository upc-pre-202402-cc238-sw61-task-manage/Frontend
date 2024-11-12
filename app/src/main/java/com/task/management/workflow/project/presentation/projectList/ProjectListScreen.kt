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
import com.task.management.workflow.project.domain.Project
import com.task.management.workflow.project.presentation.ProjectViewModel
import com.task.management.workflow.project.presentation.dialogs.ProjectCreationDialog
import com.task.management.workflow.ui.theme.DeepIndigoColor
import com.task.management.workflow.ui.theme.SlateBlueColor

@Composable
fun ProjectListScreen(viewModel: ProjectViewModel, navController: NavController) {
    val state = viewModel.state.value
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getProjects()
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
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                state.data?.let { projectList: List<Project> ->
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
                viewModel.createProject(name, description, leader)
                viewModel.getProjects()
            },
            onDismissRequest = { showDialog = false }
        )
    }
}