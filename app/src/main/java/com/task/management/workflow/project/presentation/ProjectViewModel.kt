package com.task.management.workflow.project.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.management.workflow.common.Resource
import com.task.management.workflow.common.UIState
import com.task.management.workflow.project.data.repository.ProjectRepository
import com.task.management.workflow.project.domain.Project
import com.task.management.workflow.task.data.repository.TaskRepository
import com.task.management.workflow.task.domain.Task
import kotlinx.coroutines.launch

class ProjectViewModel(
    private val repository: ProjectRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val _state = mutableStateOf(UIState<List<Project>>())
    val state: State<UIState<List<Project>>> get() = _state

    private val _selectedProject = mutableStateOf<Project?>(null)
    val selectedProject: State<Project?> get() = _selectedProject

    private val _tasks = mutableStateOf<List<Task>>(emptyList())
    val tasks: State<List<Task>> get() = _tasks

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

    suspend fun deleteProject(project: Project) {
        viewModelScope.launch {
            val projectId = project.projectId?: 0
            val result = repository.deleteRemotely(projectId)
            if (result is Resource.Success) {
                _state.value = UIState(
                    isLoading = false,
                    data = _state.value.data?.filter { it.projectId != project.projectId }
                )
            }
        }
    }

    fun loadProjectTasks(projectId: Long, status: String? = null) {
        viewModelScope.launch {
            val result = taskRepository.getTasksByProjectAndStatus(projectId,status)
            if (result is Resource.Success) {
                _tasks.value = result.data ?: emptyList()
            } else {
                _tasks.value = emptyList()
            }
        }
    }

}