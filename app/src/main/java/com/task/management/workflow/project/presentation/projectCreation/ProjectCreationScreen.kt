package com.task.management.workflow.project.presentation.projectCreation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.task.management.workflow.project.domain.Project

@Composable
fun ProjectScreen(viewModel: ProjectCreationViewModel, navController: NavController) {
    val state = viewModel.state.value
    val name = viewModel.name.value
    val description = viewModel.description.value

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            }
            Text(text = "Add Project")
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                value = name,
                onValueChange = {
                    viewModel.onNameChanged(it)
                },
                label = { Text(text = "Name") }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                value = description,
                onValueChange = {
                    viewModel.onDescriptionChanged(it)
                },
                label = { Text(text = "Description") }
            )
            Button(
                modifier = Modifier.padding(6.dp),
                onClick = {
                    val project = Project(
                        title = name,
                        description = description,
                        member = "",
                        leader = "",
                        createdAt = ""
                    )
                    viewModel.createProject(0, project)
                }
            ) {
                Text(text = "Create Project")
            }
            LazyColumn {
                state.projects?.let { projects ->
                    items(projects.size) { index ->
                        val project = projects[index]
                        ProjectCard(project = project, onClick = {  })
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectCard(project: Project, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = project.title)
            Text(text = project.description)
            Text(text = "Member: ${project.member}")
            Text(text = "Leader: ${project.leader}")
            Text(text = "Created At: ${project.createdAt}")
        }
    }
}