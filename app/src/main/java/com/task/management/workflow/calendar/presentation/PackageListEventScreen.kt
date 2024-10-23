package com.task.management.workflow.calendar.presentation

import android.widget.CalendarView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.task.management.workflow.calendar.domain.EventPackage

@Composable
fun PackageListEventScreen(viewModel: PackageListEventsViewModel, navController: NavController) {

    val state = viewModel.state.value
    val userId = viewModel.userId.value
    val color = Color(25, 23, 89)
    var userIdInput by remember { mutableStateOf(TextFieldValue(userId.toString())) }

    var showDialogAddEvent by remember { mutableStateOf(false) }
    var showDialogDeleteEvent by remember { mutableStateOf(false) }
    var eventToDelete by remember { mutableStateOf<EventPackage?>(null) }


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialogAddEvent = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Event")
            }
        }
    ) { paddingValues: PaddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TextField(
                modifier = Modifier.width(80.dp),
                value = userIdInput,
                onValueChange = {
                    userIdInput = it
                    val newUserId = it.text.toIntOrNull() ?: 0
                    viewModel.onUserIdChanged(newUserId)
                },
                label = { Text("User ID") }
            )

            OutlinedButton(onClick = {
                navController.navigate("profiles")
            }) {
                Icon(imageVector = Icons.Default.Person, contentDescription = "View Profile")
                Text("View Profile")
            }

            AndroidView(
                factory = { context ->
                    CalendarView(context).apply {

                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                update = { calendarView ->
                    state.data?.let { eventList ->
                    }
                }
            )
            Text(
                modifier = Modifier.padding(horizontal = 30.dp),
                text = "Lista de eventos",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            when {
                state.isLoading -> {
                    CircularProgressIndicator()
                }
                state.data != null -> {

                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = 30.dp)
                            .weight(1f)
                    ) {

                        items(state.data ?: emptyList()) { event ->
                            Column(
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = event.title, fontSize = 16.sp, color = color, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    IconButton(onClick = {
                                        eventToDelete = event
                                        showDialogDeleteEvent = true}) {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            tint = Color.Red,
                                            contentDescription = "Eliminar evento"
                                        )
                                    }
                                }
                                Text(text = event.title, fontSize = 22.sp, color = color)
                                Text(text = event.formattedDate(), color = color)
                                Text(
                                    text = event.description,
                                    fontSize = 15.sp,
                                    color = color,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
                state.error.isNotEmpty() -> {
                    Text(text = "Error: ${state.error}")
                }
            }
        }
    }

    if (showDialogAddEvent) {
        AddEventDialog(
            onDismiss = { showDialogAddEvent = false },
            onConfirm = { title, description, day, month, year ->
                viewModel.addEvent(title, description, day, month, year)
                showDialogAddEvent = false
            }
        )
    }

    if(showDialogDeleteEvent){
        DeleteEventDialog(
            eventTitle = eventToDelete!!.title,
            onDismiss = { showDialogDeleteEvent = false },
            onConfirm = {
                viewModel.removeEvent(eventToDelete!!.id)
                showDialogDeleteEvent = false
                eventToDelete = null
            }
        )
    }

}