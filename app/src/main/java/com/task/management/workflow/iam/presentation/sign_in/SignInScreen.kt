package com.task.management.workflow.iam.presentation.sign_in

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

@Composable
fun SignInScreen(viewModel: SignInViewModel, navController: NavController) {
    val user = viewModel.user.value

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = viewModel.username.collectAsState().value,
                onValueChange = { viewModel.onUsernameChanged(it) },
                label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
            OutlinedTextField(
                value = viewModel.password.collectAsState().value,
                onValueChange = { viewModel.onPasswordChanged(it) },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
            OutlinedButton(
                onClick = {
                    viewModel.signIn().let {
                        navController.navigate("packageList") // Navigate to PackageListEventScreen
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Text("Sign in")
            }

            OutlinedButton(
                onClick = {
                    navController.navigate("signUp") // Navigate to SignUpScreen
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Text("Not have an account? Sign up")
            }

            if (user.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                user.data?.let {
                    Text("Welcome ${it.username}", modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                Text(user.error, modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}

@Preview
@Composable
fun IAMScreenPreview() {
}