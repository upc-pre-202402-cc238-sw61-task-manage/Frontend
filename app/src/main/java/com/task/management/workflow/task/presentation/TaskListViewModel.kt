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

    private var defaultState = "ALL"

    private val _statusItem = mutableStateOf(defaultState)
    val statusItem: State<String> get() = _statusItem

    private val _name = mutableStateOf("")
    val name: State<String> get() = _name

    private val _description = mutableStateOf("")
    val description: State<String> get() = _description

    private val _dueDate = mutableStateOf<String>("")
    val dueDate: State<String> get() = _dueDate

    private val _userId = mutableLongStateOf(1)
    val userId: State<Long> get() = _userId

    private val _projectId = mutableLongStateOf(1)
    val projectId: State<Long> get() = _projectId

    private val _onlyShowUser = mutableStateOf(false)
    val onlyShowUser: State<Boolean> get() = _onlyShowUser

    fun onStatusItemChanged(status: String){
        _statusItem.value = status
    }

    fun onOnlyShowUserChanged(onlyShowUser: Boolean){
        _onlyShowUser.value = onlyShowUser
    }

    fun searchTasks(){
        if(this.onlyShowUser.value) {
            if(statusItem.value == defaultState) {
                getTasksFromProjectWithUserId(projectId.value,userId.value)
            }
            else {
                getTasksFromProjectWithUserId(projectId.value,userId.value, statusItem.value)
            }
        }
        else {
            if(statusItem.value == defaultState){
                getTasksFromProject(projectId.value)
            } else {
                getTasksFromProject(projectId.value,statusItem.value)
            }
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

    private fun getTasksFromProject(projectId: Long, status: String? = null) {
        fetchTasks { repository.getTasksByProjectAndStatus(projectId, status) }
    }

    private fun getTasksFromProjectWithUserId(projectId: Long, userId: Long, status: String? = null) {
        fetchTasks { repository.getTasksByProjectAndUserAndStatus(projectId, userId, status) }
    }

    fun createTask() {
        viewModelScope.launch {
            val newTask = Task(
                name = name.value,
                description = description.value,
                dueDate = dueDate.value,
                userId = userId.value,
                projectId = projectId.value,
                status = TaskStatus.NEW
            )
            val result = repository.postTask(newTask)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            val result = repository.putTask(task)
            if (result is Resource.Success) {
                searchTasks()
            }
        }
    }

    fun deleteTask(task: Task){
        viewModelScope.launch {
            val taskId = task.taskId?: 0
            val result = repository.deleteTask(taskId)
            if (result is Resource.Success) {
                _state.value = UIState(
                    isLoading = false,
                    data = _state.value.data?.filter { it.taskId != task.taskId }
                )
            } else {
                _state.value = UIState(
                    isLoading = false,
                    error = result.message ?: "Unknown error"
                )
            }
        }
    }
}