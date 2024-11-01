package com.task.management.workflow.iam.presentation.account_selector

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.task.management.workflow.iam.presentation.sign_in.SignInViewModel

@Composable
fun AccountSelectorScreen(signInViewModel: SignInViewModel, navController: NavController) {
    var userList = signInViewModel.userList.value
    var selectedUser = signInViewModel.user.value
    var mSelectedText = remember { mutableStateOf(selectedUser.data?.username ?: "") }
    var expanded by remember { mutableStateOf(false) }


    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Scaffold(
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column {
                OutlinedTextField(
                    value = mSelectedText.value,
                    onValueChange = { mSelectedText.value = it },
                    label = { Text("Select an account") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            mTextFieldSize = coordinates.size.toSize()
                        },
                    readOnly = true,
                    trailingIcon = {
                        Icon(icon, "contentDescription",
                            Modifier.clickable { expanded = !expanded })
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
                ) {
                    userList.data?.forEach { account ->
                        DropdownMenuItem(
                            onClick = {
                                mSelectedText.value = account.username
                                signInViewModel.onPasswordChanged(account.password)
                                expanded = false
                            },
                            text = { Text(account.username) }
                        )
                    }
                }
                Row {
                    OutlinedButton(
                        onClick = {
                            signInViewModel.onUsernameChanged(mSelectedText.value)
                            navController.navigate("signIn")
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Sign in", color = MaterialTheme.colorScheme.primary)
                    }
                    OutlinedButton(
                        onClick = {
                            navController.navigate("signUp")
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Or just do a normal Sign Up", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
