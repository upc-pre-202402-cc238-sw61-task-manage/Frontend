package com.task.management.workflow.calendar.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.management.workflow.calendar.data.repository.PackageRepository
import com.task.management.workflow.calendar.domain.EventPackage
import com.task.management.workflow.common.Resource
import com.task.management.workflow.common.UIState
import kotlinx.coroutines.launch

class PackageListEventsViewModel(private val repository: PackageRepository): ViewModel() {

    private val _state = mutableStateOf(UIState<List<EventPackage>>())
    val state: State<UIState<List<EventPackage>>> get() = _state

    private val _userId = mutableStateOf(0)
    val userId: State<Int> get() = _userId

    fun onUserIdChanged(id: Int){
        _userId.value = id
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
}