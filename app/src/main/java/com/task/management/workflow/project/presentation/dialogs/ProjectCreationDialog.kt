package com.task.management.workflow.project.presentation.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.task.management.workflow.ui.theme.FailureColor

@Composable
fun ProjectCreationDialog(
    onCreateProject: (String, String, String) -> Unit,
    onDismissRequest: () -> Unit,
){
    var newName by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }
    var newLeader by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Create Project") },
        text = {
            Column {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Title") }
                )
                OutlinedTextField(
                    value = newDescription,
                    onValueChange = { newDescription = it },
                    label = { Text("Description") }
                )
                OutlinedTextField(
                    value = newLeader,
                    onValueChange = { newLeader = it },
                    label = { Text(text = "Project Leader") }
                )
            }
        },
        confirmButton = {
            Button(
                modifier = Modifier.padding(start = 8.dp),
                onClick = {
                    onCreateProject(newName, newDescription, newLeader)
                    onDismissRequest()
                }
            ) {
                Text(text = "Create Project")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissRequest,
                colors = ButtonColors(
                    FailureColor,
                    Color.White,
                    Color.Gray,
                    Color.Gray
                )
            ) {
                Text("Cancel")
            }
        }
    )
}