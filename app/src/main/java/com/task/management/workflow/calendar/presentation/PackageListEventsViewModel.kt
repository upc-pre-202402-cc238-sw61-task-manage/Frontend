package com.task.management.workflow.calendar.presentation

import android.icu.util.Calendar
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.management.workflow.calendar.data.repository.PackageRepository
import com.task.management.workflow.calendar.domain.CreateEventRequest
import com.task.management.workflow.calendar.domain.EventPackage
import com.task.management.workflow.common.Resource
import com.task.management.workflow.common.UIState
import kotlinx.coroutines.launch

class PackageListEventsViewModel(private val repository: PackageRepository): ViewModel() {

    private val _state = mutableStateOf(UIState<List<EventPackage>>())
    val state: State<UIState<List<EventPackage>>> get() = _state

    private val _userId = mutableIntStateOf(1)
    val userId: State<Int> get() = _userId

    val calendar: Calendar = Calendar.getInstance()

    fun onUserIdChanged(id: Int){
        _userId.intValue = id
        getPackages()
    }

    private fun getPackages(){
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = repository.getPackages(userId.value)

            if(result is Resource.Success){
                _state.value = UIState(data = result.data)
            }else{
                _state.value = UIState(error = result.message?:"An error occurred")
            }
        }

    }
    fun addEvent(title: String, description: String, day: Int, month: Int, year: Int) {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val newEvent = CreateEventRequest(0, _userId.intValue, title, description, day, month, year)

            val result = repository.addEvent(newEvent)

            if (result is Resource.Success) {
                getPackages()
            } else {
                _state.value = UIState(error = result.message ?: "An error occurred")
            }
        }
    }

    fun removeEvent(eventId: Int) {
        viewModelScope.launch {
            val result = repository.deleteEvent(eventId)
            if (result is Resource.Success) {
                getPackages()
            } else {
                _state.value = UIState(error = result.message ?: "An error occurred")
            }
        }
    }

}