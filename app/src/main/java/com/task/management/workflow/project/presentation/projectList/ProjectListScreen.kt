package com.task.management.workflow.project.presentation.projectList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.task.management.workflow.project.domain.Project
import com.task.management.workflow.project.presentation.projectCreation.ProjectCreationViewModel

@Composable
fun ProjectListScreen(viewModel: ProjectCreationViewModel, navController: NavController) {
    val state = viewModel.state.value

    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn {
                    state.projects?.let { projects: List<Project> ->
                        items(projects.size) { index ->
                            val project = projects[index]
                            ProjectCard(project = project)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectCard(project: Project) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF302DA0))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = project.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(text = project.description, color = Color.White)
            Text(text = "Member: ${project.member}", color = Color.White)
            Text(text = "Leader: ${project.leader}", color = Color.White)
            Text(text = "Created At: ${project.createdAt}", color = Color.White)
        }
    }
}