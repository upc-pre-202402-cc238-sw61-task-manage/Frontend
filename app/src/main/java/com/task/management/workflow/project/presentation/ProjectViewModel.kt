package com.task.management.workflow.project.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.management.workflow.common.Resource
import com.task.management.workflow.common.UIState
import com.task.management.workflow.project.data.repository.ProjectRepository
import com.task.management.workflow.project.domain.Project
import com.task.management.workflow.projectUser.data.repository.ProjectUserRepository
import com.task.management.workflow.task.data.remote.TaskDto
import com.task.management.workflow.task.data.remote.toTask
import com.task.management.workflow.task.data.repository.TaskRepository
import com.task.management.workflow.task.domain.Task
import com.task.management.workflow.task.domain.TaskStatus
import kotlinx.coroutines.launch

class ProjectViewModel(
    private val repository: ProjectRepository,
    private val taskRepository: TaskRepository,
    private val projectUserRepository: ProjectUserRepository
) : ViewModel() {
    private val _state = mutableStateOf(UIState<List<Project>>())
    val state: State<UIState<List<Project>>> get() = _state

    private val _selectedProject = mutableStateOf<Project?>(null)
    val selectedProject: State<Project?> get() = _selectedProject

    private val _taskList = mutableStateOf(UIState<List<Task>>())
    val taskList: State<UIState<List<Task>>> get() = _taskList

    private var defaultStatusState = "ALL"

    init {
        viewModelScope.launch {
            getProjects()
        }
    }

    fun getProjects() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = repository.getAllRemotely()
            if (result is Resource.Success && !result.data.isNullOrEmpty()) {
                _state.value = UIState(data = result.data)
            } else {
                _state.value = UIState(error = "An error occurred")
            }
        }
    }

    fun getProjectById(projectId: Long) {
        _selectedProject.value = _state.value.data?.find { it.projectId == projectId }
    }

    fun createProject(newTitle: String, newDescription: String, newLeader: String ) {
        viewModelScope.launch {
            val newProject = Project(
                title = newTitle,
                description = newDescription,
                leader = newLeader
            )
            repository.insertRemotely(newProject)
        }
    }

    fun updateProject(project: Project) {
        viewModelScope.launch {
            repository.updateRemotely(project)
            _selectedProject.value = project
        }
    }

    fun deleteProject(projectId: Long) {
        viewModelScope.launch {
            repository.deleteRemotely(projectId)
            projectUserRepository.deleteUsersByProjectId(projectId)

        }
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

    fun getTasks(projectId: Long, status: String, onlyShowUser: Boolean){
        if(onlyShowUser) {
            if(status == defaultStatusState) {
                getTasksFromProjectWithUserId(projectId,1)
            }
            else {
                getTasksFromProjectWithUserId(projectId,1, status)
            }
        }
        else {
            if(status == defaultStatusState){
                getTasksFromProject(projectId)
            } else {
                getTasksFromProject(projectId,status)
            }
        }
    }

    private fun fetchTasks(
        fetchFunction: suspend () -> Resource<List<Task>>
    ) {
        _taskList.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = fetchFunction()
            if (result is Resource.Success) {
                _taskList.value = UIState(data = result.data)
            } else {
                _taskList.value = UIState(error = result.message ?: "An error occurred")
            }
        }
    }

    private fun getTasksFromProject(projectId: Long, status: String? = null) {
        fetchTasks { taskRepository.getTasksByProjectAndStatusRemotely(projectId, status) }
    }

    private fun getTasksFromProjectWithUserId(projectId: Long, userId: Long, status: String? = null) {
        fetchTasks { taskRepository.getTasksByProjectAndUserAndStatusRemotely(projectId, userId, status) }
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
           if(result is Resource.Success){
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

}