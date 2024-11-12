package com.task.management.workflow.projectUser.presentation.dialogs

/*

@Composable
fun ProjectUserCreationDialog(
    projectId: Long,
    projectUserViewModel: ProjectUserViewModel,
    onDismissRequest: () -> Unit
) {
    val users by projectUserViewModel.state
    val userIdToExclude = 1L

    LaunchedEffect(projectId) {
        projectUserViewModel.getUsers(projectId, userIdToExclude)
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Add Users to Project") },
        text = {
            Column {
                users.data?.forEach { user ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                projectUserViewModel.addUserToProject(projectId, user.userId)
                            }
                            .padding(8.dp)
                    ) {
                        Text(text = user.userId.toString())
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add User",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismissRequest) {
                Text("Done")
            }
        }
    )
}

 */