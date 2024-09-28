package com.task.management.workflow.iam.presentation.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(viewModel: SignUpViewModel, navController: NavController) {
    val user = viewModel.user.value
    val roles = listOf("ROLE_USER", "ROLE_ADMIN")
    var selectedRole = viewModel.roles.collectAsState().value
    selectedRole = selectedRole.ifEmpty { roles[0] }
    val expanded = remember { mutableStateOf(false) }

    Scaffold() { paddingValues ->
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


            ExposedDropdownMenuBox(
                expanded = expanded.value,
                onExpandedChange = { expanded.value = !expanded.value }
            ) {
                    OutlinedTextField(
                        value = selectedRole,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Role") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                    )
                DropdownMenu(
                        expanded = expanded.value,
                    onDismissRequest = { expanded.value = false }
                ) {
                    roles.forEach { role ->
                        DropdownMenuItem(
                            text = { Text(role) },
                            onClick = {
                                selectedRole = role
                                viewModel.onRolesChanged(selectedRole)
                                    expanded.value = false
                            }
                        )
                    }
                }
            }

            OutlinedButton(
                onClick = { viewModel.signUp() },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Text("Sign in")
            }

            OutlinedButton(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
                navController.navigate("signIn")
            }) {
                Text("Already have an account? Sign in")
            }

            if (user.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (user.error.isNotEmpty()) {
                Text(user.error, modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                user.data?.let {
                    LaunchedEffect(it) {
                        navController.navigate("packageList")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SignUpScreenPreview() {
}