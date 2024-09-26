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
import androidx.compose.ui.tooling.preview.Preview
import com.task.management.workflow.iam.data.remote.IAMService
import com.task.management.workflow.iam.data.repository.IAMRepository
import com.task.management.workflow.common.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun IAMScreen (viewModel: IAMViewModel){
    val user = viewModel.user.value

    Scaffold() { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            OutlinedTextField(value = viewModel.username.collectAsState().value, onValueChange = {
                viewModel.onUsernameChanged(it)
            })
            OutlinedTextField(value = viewModel.password.collectAsState().value, onValueChange = {
                viewModel.onPasswordChanged(it)
            })
            OutlinedButton(onClick = {
                viewModel.signIn()
            }) {
                Text("Sign in")
            }

            if (user.isLoading) {
                CircularProgressIndicator()
            } else {
                user.data?.let {
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
    val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(
        GsonConverterFactory.create()).build()
    val service = retrofit.create(IAMService::class.java)
    val repository = IAMRepository(service)
    val viewModel = IAMViewModel(repository)
    IAMScreen(viewModel)
}