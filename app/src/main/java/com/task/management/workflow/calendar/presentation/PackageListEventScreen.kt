package com.task.management.workflow.calendar.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.daysOfWeek
import com.task.management.workflow.calendar.domain.EventPackage
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PackageListEventScreen(viewModel: PackageListEventsViewModel, navController: NavController) {

    val highlightedDates1 = listOf(
        LocalDate.of(2024, 10, 23),
        LocalDate.of(2024, 11, 5)
    )

    val highlightedDates2 = listOf(
        LocalDate.of(2024, 12, 25),
        LocalDate.of(2024, 12, 31),
        LocalDate.of(2024, 10, 23)
    )

    val events = viewModel.events.value
    val userId = viewModel.userId.value
    val color = Color(25, 23, 89)
    var userIdInput by remember { mutableStateOf(TextFieldValue(userId.toString())) }

    var showDialogAddEvent by remember { mutableStateOf(false) }
    var showDialogDeleteEvent by remember { mutableStateOf(false) }
    var eventToDelete by remember { mutableStateOf<EventPackage?>(null) }

    //Lista de fechas de eventos para calendario
    val eventsDate = viewModel.eventDates
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(100) } // Adjust as needed
    val daysOfWeek = daysOfWeek()

    val estate = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )


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


            //Calendario
            HorizontalCalendar(
                state = estate,
                dayContent = { Day(it, eventsDate, highlightedDates2) },
                monthContainer = { _, container ->
                    val configuration = LocalConfiguration.current
                    val screenWidth = configuration.screenWidthDp.dp
                    Box(
                        modifier = Modifier
                            .width(screenWidth * 0.99f)
                            .padding(8.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .border(
                                color = color,
                                width = 1.dp,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        container() // Render the provided container!
                    }
                },
                monthHeader = {
                    DaysOfWeekTitle(daysOfWeek = daysOfWeek) // Use the title as month header
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
                events.isLoading -> {
                    CircularProgressIndicator()
                }
                events.data != null -> {

                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = 30.dp)
                            .weight(1f)
                    ) {

                        items(events.data ?: emptyList()) { event ->
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

                                Text(text = event.dueDate, color = color)
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
                events.error.isNotEmpty() -> {
                    Text(text = "Error: ${events.error}")
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Day(day: CalendarDay, eventsDates: List<LocalDate>, highlightedDates2: List<LocalDate>) {

    val color = Color(25, 23, 89)

    val isHighlighted1 = eventsDates.contains(day.date)
    val isHighlighted2 = highlightedDates2.contains(day.date)
    val isHighlightedBoth = isHighlighted1 && isHighlighted2

    val backgroundColor = when {
        isHighlightedBoth -> Color.Magenta
        isHighlighted1 -> color
        isHighlighted2 -> Color.Green
        else -> Color.Transparent
    }

    val textColor = when {
        isHighlightedBoth -> Color.White
        isHighlighted1 -> Color.White
        isHighlighted2 -> Color.Blue
        else -> color
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .background(backgroundColor)
            .padding(2.dp)
            .shadow(1.dp, shape = RoundedCornerShape(1.dp))
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = textColor
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
            )
        }
    }
}
