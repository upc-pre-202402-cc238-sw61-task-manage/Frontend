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
import com.task.management.workflow.project.domain.Project
import com.task.management.workflow.ui.theme.FailureColor

@Composable
fun ProjectEditionDialog(
    project: Project,
    onConfirm: (String, String) -> Unit,
    onDismissRequest: () -> Unit
) {
    var newTitle by remember { mutableStateOf(project.title) }
    var newDescription by remember { mutableStateOf(project.description) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Edit Project") },
        text = {
            Column {
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    label = { Text("Title") }
                )
                OutlinedTextField(
                    value = newDescription,
                    onValueChange = { newDescription = it },
                    label = { Text("Description") }
                )
            }
        },
        confirmButton = {
            Button(
                modifier = Modifier.padding(start = 8.dp),
                onClick = {
                    onConfirm(newTitle, newDescription)
                    onDismissRequest()
                }
            ) {
                Text(text = "Save")
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