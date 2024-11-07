package com.task.management.workflow.calendar.presentation

import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.management.workflow.calendar.data.repository.PackageRepository
import com.task.management.workflow.calendar.domain.CreateEventRequest
import com.task.management.workflow.calendar.domain.EventPackage
import com.task.management.workflow.common.Resource
import com.task.management.workflow.common.UIState
import com.task.management.workflow.task.data.repository.TaskRepository
import com.task.management.workflow.task.domain.Task
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class PackageListEventsViewModel(private val repository: PackageRepository, private val repository2: TaskRepository): ViewModel() {

    private val _events = mutableStateOf(UIState<List<EventPackage>>())
    val events: State<UIState<List<EventPackage>>> get() = _events

    private val _tasks = mutableStateOf(UIState<List<Task>>())
    val tasks: State<UIState<List<Task>>> get() = _tasks

    private val _userId = mutableIntStateOf(1)
    val userId: State<Int> get() = _userId

    val calendar: Calendar = Calendar.getInstance()

    //Lista de fecha de eventos de calendario
    private val _eventDates = mutableStateListOf<LocalDate>()
    val eventDates: List<LocalDate> get() = _eventDates

    private val _taskDates = mutableStateListOf<LocalDate>()
    val taskDates: List<LocalDate> get() = _taskDates

    fun onUserIdChanged(id: Int){
        _userId.intValue = id
        getTasksPackages()
        getEventsPackages()

    }

    private fun getEventsPackages(){
        _events.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = repository.getPackages(userId.value)

            if(result is Resource.Success){
                _events.value = UIState(data = result.data)

                _eventDates.clear()
                result.data?.forEach { event ->
                    val eventDate = event.toLocalDate() // Utilizando la función toLocalDate
                    _eventDates.add(eventDate)}

            }else{
                _events.value = UIState(error = result.message?:"An error occurred")
            }
        }
    }

    private fun getTasksPackages(){
        _tasks.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = repository2.getTasksByUserId(userId.value)

            if(result is Resource.Success){
                _tasks.value = UIState(data = result.data)

                _taskDates.clear()
                result.data?.forEach { task ->
                    val taskDate = task.toLocalDate()
                    _taskDates.add(taskDate)}
            }else{
                _tasks.value = UIState(error = result.message?:"An error occurred")
            }
        }
    }

    fun addEvent(title: String, description: String, duedate: String) {
        _events.value = UIState(isLoading = true)
        viewModelScope.launch {
            val newEvent = CreateEventRequest(0, _userId.intValue, title, description, duedate)

            val result = repository.addEvent(newEvent)

            if (result is Resource.Success) {
                getEventsPackages()
            } else {
                _events.value = UIState(error = result.message ?: "An error occurred")
            }
        }
    }

    fun removeEvent(eventId: Int) {
        viewModelScope.launch {
            val result = repository.deleteEvent(eventId)
            if (result is Resource.Success) {
                getEventsPackages()
            } else {
                _events.value = UIState(error = result.message ?: "An error occurred")
            }
        }
    }

    fun editEvent(eventId: Int, title: String, description: String, duedate: String){
        _events.value = UIState(isLoading = true)
        viewModelScope.launch {
            val newEvent = CreateEventRequest(0, _userId.intValue, title, description, duedate)
            val result = repository.editEvent(eventId, newEvent)
            if (result is Resource.Success) {
                getEventsPackages()
            } else {
                _events.value = UIState(error = result.message ?: "An error occurred")
            }
        }
    }



}