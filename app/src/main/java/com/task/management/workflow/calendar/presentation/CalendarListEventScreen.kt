package com.task.management.workflow.calendar.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.task.management.workflow.calendar.domain.EventPackage
import com.task.management.workflow.common.session.UserSession
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarListEventScreen(viewModel: CalendarListEventsViewModel) {
    // Using the UserSession to get the user ID
    val userId = UserSession.userId.collectAsState().value

    val events = viewModel.events.value
    val color = Color(25, 23, 89)
    //TODO: Delete unused code
    //var userIdInput by remember { mutableStateOf(TextFieldValue(userId.toString())) }

    var showDialogAddEvent by remember { mutableStateOf(false) }
    var showDialogDeleteEvent by remember { mutableStateOf(false) }
    var eventToDelete by remember { mutableStateOf<EventPackage?>(null) }
    var showDialogEditEvent by remember { mutableStateOf(false) }
    var eventToEdit by remember { mutableStateOf<EventPackage?>(null) }

    //Lista de fechas de eventos para calendario
    val eventsDate = viewModel.eventDates
    val tasksDate = viewModel.taskDates
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
            //TODO: Delete unused code
            /*TextField(
                modifier = Modifier.width(80.dp),
                value = userIdInput,
                onValueChange = {
                    userIdInput = it
                    val newUserId = it.text.toIntOrNull() ?: 0
                    viewModel.onUserIdChanged(newUserId)
                },
                label = { Text("User ID") }
            )*/

            //Calendario
            var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
            HorizontalCalendar(
                state = estate,
                dayContent = { day ->
                    Day(day, eventsDate, tasksDate,  isSelected = selectedDate == day.date) {
                            _ -> selectedDate = if (selectedDate == day.date) null else day.date
                    }},
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
                                width = 2.dp,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        container() // Render the provided container!
                    }
                },
                monthHeader = {
                    MonthHeader(month = estate.firstVisibleMonth.yearMonth)
                    DaysOfWeekTitle(daysOfWeek = daysOfWeek)// Use the title as month header
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
                                        eventToEdit = event
                                        showDialogEditEvent = true
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            tint = Color.Blue,
                                            contentDescription = "Editar evento"
                                        )
                                    }
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
            onConfirm = { title, description, duedate ->
                viewModel.addEvent(title, description, duedate)
                showDialogAddEvent = false
            }
        )
    }

    if (showDialogEditEvent && eventToEdit != null) {
        EditEventDialog(
            event = eventToEdit!!,
            onDismiss = { showDialogEditEvent = false },
            onConfirm = { title, description, duedate ->
                viewModel.editEvent(eventToEdit!!.id, title, description, duedate)
                showDialogEditEvent = false
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
fun Day(day: CalendarDay, eventsDates: List<LocalDate>, tasksDates: List<LocalDate>, isSelected: Boolean, onClick: (CalendarDay) -> Unit) {

    val color = Color(25, 23, 89) // color events
    val color2 = Color(49, 114, 112) // color tasks
    val color3 = Color(78, 2, 80)

    val isHighlighted1 = eventsDates.contains(day.date)
    val isHighlighted2 = tasksDates.contains(day.date)
    val isHighlightedBoth = isHighlighted1 && isHighlighted2

    val backgroundColor = when {
        isHighlightedBoth -> color3
        isHighlighted1 -> color
        isHighlighted2 -> color2
        else -> Color.Transparent
    }

    val textColor = when {
        isHighlightedBoth -> Color.White
        isHighlighted1 -> Color.White
        isHighlighted2 -> Color.White
        else -> color
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .shadow(1.dp, shape = RoundedCornerShape(1.dp))
            .background(color = if (isSelected) Color(241,135,1) else backgroundColor)
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) }
            ),
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthHeader(month: YearMonth) {
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
    Text(
        text = month.format(formatter),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        textAlign = TextAlign.Center
    )
}