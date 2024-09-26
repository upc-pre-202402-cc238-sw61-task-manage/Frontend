package com.task.management.workflow.task.presentation.taskCreation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.management.workflow.task.data.remote.TaskService
import com.task.management.workflow.task.data.repository.TaskRepository
import com.task.management.workflow.task.domain.Task
import kotlinx.coroutines.launch
import java.util.Date

class TaskCreationViewModel(private val repository: TaskRepository): ViewModel() {
    private val _state = mutableStateOf(TaskCreationState())
    val state: State<TaskCreationState> get() = _state

    private val _taskId = mutableLongStateOf(0)
    val taskId: State<Long> get() = _taskId

    private val _name = mutableStateOf("")
    val name: State<String> get() = _name

    private val _description = mutableStateOf("")
    val description: State<String> get() = _description

    private val _dueDate = mutableStateOf<String?>("")
    val dueDate: State<String?> get() = _dueDate

    private val _userId = mutableLongStateOf(0)
    val userId: State<Long> get() = _userId

    private val _projectId = mutableLongStateOf(0)
    val projectId: State<Long> get() = _projectId

    fun onTaskIdChanged(newValue: String) {
        val newLongValue = newValue.toLongOrNull()
        if (newLongValue != null) {
            _taskId.longValue = newLongValue
        }
    }

    fun onNameChanged(name: String) {
        _name.value = name
    }

    fun onDescriptionChanged(description: String) {
        _description.value = description
    }

    fun onDueDateChanged(dueDate: String) {
        _dueDate.value = dueDate
    }

    fun onUserIdChanged(newValue: String) {
        val newLongValue = newValue.toLongOrNull()
        if (newLongValue != null) {
            _userId.longValue = newLongValue
        }
    }

    fun onProjectIdChanged(newValue: String) {
        val newLongValue = newValue.toLongOrNull()
        if (newLongValue != null) {
            _projectId.longValue = newLongValue
        }

    }

    fun createTask(id:Long, task: Task){
        viewModelScope.launch {
            if(task.name.isEmpty() || task.description.isEmpty()){
                return@launch
            }
            if (task.description.length > 200 || task.name.length > 40) {
                return@launch
            }
            repository.insert(id,task.name,task.description,task.dueDate,task.userID,task.projectID)
        }
    }


    fun updateTask(id: Long, task: Task){
        viewModelScope.launch {
            if(task.name.isEmpty() || task.description.isEmpty()){
                return@launch
            }
            if (task.description.length > 200 || task.name.length > 40) {
                return@launch
            }
            repository.update(id,task.name,task.description,task.dueDate,task.userID,task.projectID)
        }
    }

    suspend fun deleteTask(id: Long, task: Task) {
        repository.delete(id,task.name,task.description,task.dueDate,task.userID,task.projectID)
    }

}