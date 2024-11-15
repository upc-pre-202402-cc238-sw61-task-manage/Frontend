package com.task.management.workflow.projectUser.presentation.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.task.management.workflow.ui.theme.SlateBlueColor
import com.task.management.workflow.user.domain.User
import com.task.management.workflow.user.presentation.UserViewModel

@Composable
fun ProjectUserCreationDialog(
    userViewModel: UserViewModel,
    onConfirm: (Long) -> Unit,
    onDismissRequest: () -> Unit
) {
    val users = userViewModel.usersState.value
    var expanded by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(Unit) {
        userViewModel.getAllUsers()
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                "Add User to Project",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Please select a user to add to the project:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { expanded = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(SlateBlueColor)
                    ) {
                        Text(
                            text = selectedUser?.username ?: "Select a user",
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        users.data?.forEach { user ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedUser = user
                                    expanded = false
                                },
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = user.username)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedUser?.id?.let { userId ->
                        onConfirm(userId)
                        onDismissRequest()
                    }
                },
                enabled = selectedUser != null,
                colors = ButtonDefaults.buttonColors(SlateBlueColor)
            ) {
                Text("Add", color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismissRequest
            ) {
                Text("Cancel")
            }
        }
    )
}
