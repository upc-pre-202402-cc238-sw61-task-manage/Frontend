package com.task.management.workflow.project.presentation.projectCreation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.task.management.workflow.project.domain.Project

@Composable
fun ProjectScreen(viewModel: ProjectCreationViewModel) {
    val state = viewModel.state.value
    val name = viewModel.name.value
    val projects = listOf(
        Project("Title1", "Description1", "Member1", "Leader1"),
        Project("Title2", "Description2", "Member2", "Leader2")
    )

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                value = name,
                onValueChange = {
                    viewModel.onNameChanged(it)
                },
                label = { Text(text = "Project Name") }
            )
            projects.forEach { project ->
                ProjectItem(project = project)
            }
        }
    }
}

@Composable
fun ProjectItem(project: Project){
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .weight(1f)
        ) {
            Text(project.title)
            Text(project.description)
            Text(project.member)
            Text(project.leader)

        }
    }
}