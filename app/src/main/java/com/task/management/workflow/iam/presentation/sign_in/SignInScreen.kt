package com.task.management.workflow.iam.presentation.sign_in

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.task.management.workflow.R
import com.task.management.workflow.common.constants.NavigationConstants

@Composable
fun SignInScreen(viewModel: SignInViewModel, navController: NavController) {
    val user = viewModel.user.value

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier.padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Sign in", style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary)
                Image(
                    painter = painterResource(id = R.drawable.workflow_logo),
                    contentDescription = "Workflow logo",
                    modifier = Modifier.size(120.dp)
                )
                OutlinedTextField(
                    value = viewModel.username.collectAsState().value,
                    onValueChange = { viewModel.onUsernameChanged(it) },
                    label = { Text("Username", color = MaterialTheme.colorScheme.primary ) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                )
                OutlinedTextField(
                    value = viewModel.password.collectAsState().value,
                    onValueChange = { viewModel.onPasswordChanged(it) },
                    label = { Text("Password", color = MaterialTheme.colorScheme.primary) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                )
                OutlinedButton(
                    onClick = {
                        viewModel.signIn()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("Sign in", color = MaterialTheme.colorScheme.secondary)
                }

                OutlinedButton(
                    onClick = {
                        navController.navigate(NavigationConstants.SIGN_UP_PATH) // Navigate to SignUpScreen
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("Not have an account? Sign up")
                }

                if (user.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (user.error.isNotEmpty()) {
                    Text(user.error, modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    user.data?.let {
                        LaunchedEffect(it) {
                            viewModel.saveInDatabase()
                            navController.navigate(NavigationConstants.PROJECT_LIST_PATH)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun IAMScreenPreview() {

}