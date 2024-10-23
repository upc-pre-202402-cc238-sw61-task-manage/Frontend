package com.task.management.workflow.task.presentation

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.task.management.workflow.ui.theme.FailureColor
import com.task.management.workflow.ui.theme.SuccessColor

@Composable
fun TaskDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Confirm Delete")
        },
        text = {
            Text("Are you sure you want to delete this task?")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonColors(
                    SuccessColor,
                    Color.White,
                    Color.Gray,
                    Color.Gray
                )
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonColors(
                    FailureColor,
                    Color.White,
                    Color.Gray,
                    Color.Gray
                )
            ) {
                Text("No")
            }
        }
    )
}