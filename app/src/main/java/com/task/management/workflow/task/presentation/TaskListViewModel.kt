package com.task.management.workflow.task.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.management.workflow.common.Resource
import com.task.management.workflow.common.UIState
import com.task.management.workflow.task.data.repository.TaskRepository
import com.task.management.workflow.task.domain.Task
import com.task.management.workflow.task.domain.TaskStatus
import kotlinx.coroutines.launch

class TaskListViewModel(private val repository: TaskRepository): ViewModel() {
    private val _state = mutableStateOf(UIState<List<Task>>())
    val state: State<UIState<List<Task>>> get() = _state

    private val _statusItemId = mutableStateOf<Short>(0)
    val statusItemId: State<Short> get() = _statusItemId

    private val _taskId = mutableLongStateOf(0)
    val taskId: State<Long> get() = _taskId

    private val _name = mutableStateOf("")
    val name: State<String> get() = _name

    private val _description = mutableStateOf("")
    val description: State<String> get() = _description

    private val _dueDate = mutableStateOf<String>("")
    val dueDate: State<String> get() = _dueDate

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

    fun onStatusItemIDChanged(id: Short, status: String){
        _statusItemId.value = id
        if(id == 1.toShort()){
            getTasks()
        } else {
            getTasksFromProject(1, status)
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

    private fun fetchTasks(
        fetchFunction: suspend () -> Resource<List<Task>>
    ) {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = fetchFunction()
            if (result is Resource.Success) {
                _state.value = UIState(data = result.data)
            } else {
                _state.value = UIState(error = result.message ?: "An error occurred")
            }
        }
    }

    private fun getTasks() {
        fetchTasks { repository.getTasks() }
    }

    private fun getTasksFromProject(projectId: Long, status: String) {
        fetchTasks { repository.getTasksByProjectAndStatus(projectId, status) }
    }

    private fun getTasksFromProjectWithUserId(projectId: Long, userId: Long, status: String) {
        fetchTasks { repository.getTasksByProjectAndUserAndStatus(projectId, userId, status) }
    }
}