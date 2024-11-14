package com.task.management.workflow.iam.presentation.sign_up

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.task.management.workflow.common.constants.NavigationConstants

@Composable
fun SignUpScreen(viewModel: SignUpViewModel, navController: NavController) {
    val user = viewModel.user.value
    val errorMessage = viewModel.errorMessage.collectAsState().value
    val roles = listOf("ROLE_USER", "ROLE_ADMIN")
    var mSelectedText by remember { mutableStateOf(roles[0]) }
    var expanded by remember { mutableStateOf(false) }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    // this initialize the role in the viewmodel
    LaunchedEffect (Unit) {
        viewModel.onRolesChanged(mSelectedText)
    }

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
                Text("Sign up", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
                AsyncImage(
                    model = "https://raw.githubusercontent.com/upc-pre-202402-cc238-sw61-task-manage/Workflow-Report/refs/heads/main/images/styles/logo-workflow.png",
                    contentDescription = "Workflow logo",
                    modifier = Modifier.size(120.dp)
                )
                OutlinedTextField(
                    value = viewModel.username.collectAsState().value,
                    onValueChange = { viewModel.onUsernameChanged(it) },
                    label = { Text("Username", color = MaterialTheme.colorScheme.primary) },
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

                OutlinedTextField(
                    value = mSelectedText,
                    onValueChange = { mSelectedText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = { expanded = !expanded }
                        )
                        .onGloballyPositioned { coordinates ->
                            mTextFieldSize = coordinates.size.toSize()
                        },
                    readOnly = true,
                    label = {Text("Choose your roles")},
                    trailingIcon = {
                        Icon(icon,"contentDescription",
                            Modifier.clickable { expanded = !expanded })
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current){mTextFieldSize.width.toDp()})
                ) {
                    roles.forEach { label ->
                        DropdownMenuItem(onClick = {
                            mSelectedText = label
                            viewModel.onRolesChanged(label)
                            expanded = false
                        },
                            text = { Text(label) })
                    }
                }

                OutlinedButton(
                    onClick = { viewModel.signUp() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("Sign up", color = MaterialTheme.colorScheme.secondary)
                }

                OutlinedButton(modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally), onClick = {
                    navController.navigate(NavigationConstants.SIGN_IN_PATH)
                }) {
                    Text("Already have an account? Sign in")
                }

                if (user.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (errorMessage.isNotEmpty()) {
                    if (errorMessage == "Unauthorized") {
                        Text(
                            "Invalid credentials, the username is taken",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                    Text(errorMessage, color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                user.data?.let {
                    LaunchedEffect(it) {
                        navController.navigate(NavigationConstants.SIGN_IN_PATH)
                    }
                }
            }
        }
    }
}
