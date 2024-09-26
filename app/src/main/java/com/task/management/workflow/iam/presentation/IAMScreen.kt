package com.task.management.workflow.iam.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun IAMScreen (viewModel: IAMViewModel){
    val user = viewModel.user.value

    Scaffold() { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            OutlinedTextField(
                value = viewModel.username.collectAsState().value,
                onValueChange = { viewModel.onUsernameChanged(it) },
                label = { Text("Username") }
            )
            OutlinedTextField(
                value = viewModel.password.collectAsState().value,
                onValueChange = { viewModel.onPasswordChanged(it) },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation()
            )
            OutlinedButton(onClick = { viewModel.signIn() }) {
                Text("Sign in")
            }

            if (user.isLoading) {
                CircularProgressIndicator()
            } else {
                user.data?.let {
                    // Here we can redirect to another screen
                    Text("Welcome ${it.username}")
                }
                Text(user.error)
            }
        }
    }
}

@Preview
@Composable
fun IAMScreenPreview() {
}