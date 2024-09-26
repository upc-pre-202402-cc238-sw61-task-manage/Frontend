package com.task.management.workflow.task.presentation.task

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.management.workflow.task.data.local.TaskEntity
import com.task.management.workflow.task.data.remote.TaskService
import com.task.management.workflow.task.data.repository.TaskRepository
import com.task.management.workflow.task.domain.Task
import kotlinx.coroutines.launch
import java.util.Date

class TaskViewModel(private val repository: TaskRepository, private val service: TaskService): ViewModel() {
    private val _state = mutableStateOf(TaskState())
    val state: State<TaskState> get() = _state


    fun createTask(task: Task){
        viewModelScope.launch {
            if(task.name.isEmpty() || task.description.isEmpty()){
                return@launch
            }
            if (task.description.length > 200 || task.name.length > 40) {
                return@launch
            }
            service.postTask(Task(task.name,task.description,task.dueDate,task.userID,task.projectID))
        }
    }

    /*
    fun updateTask(task: Task){
        viewModelScope.launch {
            if(task.name.isEmpty() || task.description.isEmpty()){
                return@launch
            }
            if (task.description.length > 200 || task.name.length > 40) {
                return@launch
            }
            service.updateTask(Task(task.name,task.description,task.dueDate,task.userID,task.projectID))
        }
    }
     */
}