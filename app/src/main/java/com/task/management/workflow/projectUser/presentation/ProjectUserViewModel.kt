package com.task.management.workflow.projectUser.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.management.workflow.common.Resource
import com.task.management.workflow.common.UIState
import com.task.management.workflow.user.domain.User
import com.task.management.workflow.project.domain.Project
import com.task.management.workflow.projectUser.data.remote.ProjectUserRequest
import com.task.management.workflow.projectUser.data.repository.ProjectUserRepository
import com.task.management.workflow.task.data.remote.TaskDto
import com.task.management.workflow.task.data.repository.TaskRepository
import com.task.management.workflow.task.domain.Task
import com.task.management.workflow.task.domain.TaskStatus
import kotlinx.coroutines.launch

class ProjectUserViewModel(
    private val projectUserRepository: ProjectUserRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _state = mutableStateOf(UIState<List<ProjectUserRequest>>())
    val state: State<UIState<List<ProjectUserRequest>>> get() = _state

    private val _projectUsers = mutableStateOf<List<User>>(emptyList())
    val projectUsers: State<List<User>> get() = _projectUsers

    private val _userProjects = mutableStateOf(UIState<List<Project>>())
    val userProjects: State<UIState<List<Project>>> get() = _userProjects

    private val _selectedProject = mutableStateOf<Project?>(null)
    val selectedProject: State<Project?> get() = _selectedProject

    private val _taskList = mutableStateOf(UIState<List<Task>>())
    val taskList: State<UIState<List<Task>>> get() = _taskList

    private val _isProjectListUpdated = mutableStateOf(false)
    val isProjectListUpdated: State<Boolean> get() = _isProjectListUpdated

    private val _isUserListUpdated = mutableStateOf(false)
    val isUserListUpdated: State<Boolean> get() = _isUserListUpdated

    private var defaultStatusState = "ALL"

    fun getUsersByProjectId(projectId: Long) {
        viewModelScope.launch {
            val result = projectUserRepository.getUsersByProjectId(projectId)
            if (result is Resource.Success) {
                _projectUsers.value = result.data ?: emptyList()
            } else {
                _projectUsers.value = emptyList()
            }
            _isUserListUpdated.value = false
        }
    }

    fun getProjectsByUserId(userId: Long){
        _userProjects.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = projectUserRepository.getProjectsByUserId(userId)
            if(result is Resource.Success && !result.data.isNullOrEmpty()) {
                _userProjects.value = UIState(data = result.data)
            } else {
                _userProjects.value = UIState(error = "An error occurred")
            }
        }
        _isProjectListUpdated.value = false
    }

    fun getProjectById(projectId: Long){
        _selectedProject.value = _userProjects.value.data?.find { it.projectId == projectId }
    }

    fun getProjectTasks(projectId: Long, status: String? = null) {
        _taskList.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = taskRepository.getTasksByProjectAndStatusRemotely(projectId, status)
            if (result is Resource.Success && !result.data.isNullOrEmpty()) {
                _taskList.value = UIState(data = result.data)
            } else {
                _taskList.value = UIState(error = "No tasks found or an error occurred")
            }
        }
    }

    fun createTask(name: String, description: String, dueDate: String, projectId: Long, userId: Long){
        _taskList.value = UIState(isLoading = true)
        viewModelScope.launch {
            val newTask = TaskDto(
                name = name,
                description = description,
                dueDate = dueDate,
                projectId = projectId,
                userId = userId,
                status = TaskStatus.NEW
            )
            val result = taskRepository.insertRemotely(newTask.toTask())
            if (result is Resource.Success){
                getProjectTasks(projectId)
            } else {
                _taskList.value = UIState(error = result.message ?: "An error occurred")
            }
        }
    }

    fun updateTask(updatedTask: Task, projectId: Long) {
        _taskList.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = taskRepository.updateRemotely(updatedTask)
            if (result is Resource.Success) {
                getProjectTasks(projectId)
            } else {
                _taskList.value = UIState(error = result.message ?: "An error occurred")
            }
        }
    }

    fun deleteTask(taskId: Long, projectId: Long){
        _taskList.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = taskRepository.deleteRemotely(taskId)
            if(result is Resource.Success){
                getProjectTasks(projectId)
            } else {
                _taskList.value = UIState(error = result.message ?: "An error occurred")
            }
        }
    }


    fun addUserToProject(projectId: Long, userId: Long) {
        viewModelScope.launch {
            projectUserRepository.addUserToProject(projectId, userId)
            _isProjectListUpdated.value = true
            _isUserListUpdated.value = true
        }
    }

    fun getTasks(projectId: Long, userId: Long, status: String, onlyShowUser: Boolean) {
        if (onlyShowUser) {
            if (status == defaultStatusState) {
                getTasksFromProjectWithUserId(projectId, userId)
            } else {
                getTasksFromProjectWithUserId(projectId, userId, status)
            }
        } else {
            if (status == defaultStatusState) {
                getTasksFromProject(projectId)
            } else {
                getTasksFromProject(projectId, status)
            }
        }
    }

    private fun getTasksFromProject(projectId: Long, status: String? = null) {
        fetchTasks { taskRepository.getTasksByProjectAndStatusRemotely(projectId, status) }
    }

    private fun getTasksFromProjectWithUserId(projectId: Long, userId: Long, status: String? = null) {
        fetchTasks { taskRepository.getTasksByProjectAndUserAndStatusRemotely(projectId, userId, status) }
    }

    private fun fetchTasks(fetchFunction: suspend () -> Resource<List<Task>>) {
        _taskList.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = fetchFunction()
            _taskList.value = if (result is Resource.Success) {
                UIState(data = result.data)
            } else {
                UIState(error = result.message ?: "An error occurred")
            }
        }
    }

    fun removeUserFromProject(projectId: Long, userId: Long) {
        viewModelScope.launch {
            projectUserRepository.removeUserFromProject(projectId, userId)
            getUsersByProjectId(projectId)
        }
    }

}
