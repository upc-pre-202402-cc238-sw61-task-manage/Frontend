package com.task.management.workflow.calendar.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.task.management.workflow.calendar.data.remote.PackageService
import com.task.management.workflow.calendar.data.repository.PackageRepository
import com.task.management.workflow.common.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun PackageListEventScreen(viewModel: PackageListEventsViewModel){

    val state = viewModel.state.value
    val userId = viewModel.userId.value

    var userIdInput by remember { mutableStateOf(TextFieldValue(userId.toString())) }

    Scaffold { paddingValues: PaddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TextField(
                value = userIdInput,
                onValueChange = {
                    userIdInput = it
                    val newUserId = it.text.toIntOrNull() ?: 0//
                    viewModel.onUserIdChanged(newUserId)
                },
                label = { Text("User ID") }
            )

            when {
                state.isLoading -> {
                    CircularProgressIndicator()
                }
                state.data != null -> {
                    LazyColumn {
                        items(state.data ?: emptyList()) { event ->
                            Text(text = event.title)
                        }
                    }
                }
                state.error != null -> {
                    Text(text = "Error: ${state.error}")
                }
            }
        }
    }
}

@Preview
@Composable
fun PackageListScreenPreview(){
    val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(PackageService::class.java)
    val repository = PackageRepository(service)
    val viewModel = PackageListEventsViewModel(repository)
    PackageListEventScreen(viewModel)
}