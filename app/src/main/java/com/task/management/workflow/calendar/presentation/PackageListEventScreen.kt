package com.task.management.workflow.calendar.presentation

import android.widget.CalendarView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.task.management.workflow.calendar.data.remote.PackageService
import com.task.management.workflow.calendar.data.repository.PackageRepository
import com.task.management.workflow.common.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun PackageListEventScreen(viewModel: PackageListEventsViewModel) {

    val state = viewModel.state.value
    val userId = viewModel.userId.value
    val _color = Color(25, 23, 89)
    var userIdInput by remember { mutableStateOf(TextFieldValue(userId.toString())) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { showDialog = true }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Event")
        }
    }) { paddingValues: PaddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TextField(
                value = userIdInput,
                onValueChange = {
                    userIdInput = it
                    val newUserId = it.text.toIntOrNull() ?: 0
                    viewModel.onUserIdChanged(newUserId)
                },
                label = { Text("User ID") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            AndroidView(
                factory = { context ->
                    CalendarView(context).apply {

                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                update = { calendarView ->
                    state.data?.let { eventList ->
                    }
                }
            )
            Text(text = "Lista de eventos", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = _color)
            when {
                state.isLoading -> {
                    CircularProgressIndicator()
                }
                state.data != null -> {

                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .weight(1f) // Para permitir que el LazyColumn ocupe el espacio restante
                    ) {

                        items(state.data ?: emptyList()) { event ->
                            Column(
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(text = event.title, fontSize = 22.sp, color = _color)
                                Text(text = event.formattedDate(), color = _color)
                                Text(
                                    text = event.description, fontSize = 18.sp, color = _color,
                                    modifier = Modifier.padding(bottom = 13.dp)
                                )
                            }
                        }
                    }
                }
                state.error != null -> {
                    Text(text = "Error: ${state.error}")
                }
            }
        }
    }

    if (showDialog) {
        AddEventDialog(
            onDismiss = { showDialog = false },
            onConfirm = { title, description, day, month, year ->
                viewModel.addEvent(title, description, day, month, year)
                showDialog = false
            }
        )
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

