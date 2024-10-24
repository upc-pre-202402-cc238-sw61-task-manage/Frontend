package com.task.management.workflow.project.presentation.projectCreation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.management.workflow.common.Resource
import com.task.management.workflow.project.data.repository.ProjectRepository
import com.task.management.workflow.project.domain.Project
import kotlinx.coroutines.launch

class ProjectCreationViewModel(private val repository: ProjectRepository) : ViewModel() {
    private val _state = mutableStateOf(ProjectCreationState())
    val state: State<ProjectCreationState> get() = _state

    private val _projectId = mutableLongStateOf(0)
    val projectId: State<Long> get() = _projectId

    private val _name = mutableStateOf("")
    val name: State<String> get() = _name

    private val _description = mutableStateOf("")
    val description: State<String> get() = _description

    private val _member = mutableStateOf("")
    val member: State<String> get() = _member

    private val _leader = mutableStateOf("")
    val leader: State<String> get() = _leader

    fun onProjectIdChanged(newValue: String) {
        val newLongValue = newValue.toLongOrNull()
        if (newLongValue != null) {
            _projectId.longValue = newLongValue
        }
    }

    fun onNameChanged(newValue: String) {
        _name.value = newValue
    }

    fun onDescriptionChanged(newValue: String) {
        _description.value = newValue
    }

    fun onMemberChanged(newValue: String) {
        _member.value = newValue
    }

    fun onLeaderChanged(newValue: String) {
        _leader.value = newValue
    }

    fun getAllProjects() {
        _state.value = ProjectCreationState(isLoading = true)
        viewModelScope.launch {
            val result = repository.getProjectFromDatabase()
            _state.value = ProjectCreationState(projects = result)
        }
    }

    init {
        getAllProjects()
    }

    fun createProject(id: Long, project: Project) {
        viewModelScope.launch {
            repository.insert(
                id,
                project.title,
                project.description,
                project.member,
                project.leader,
                project.createdAt
            )
        }
    }

    fun updateProject(id: Long, project: Project) {
        viewModelScope.launch {
            if (project.title.isEmpty() || project.description.isEmpty()) {
                return@launch
            }
            if (project.description.length > 250 || project.title.length > 50) {
                return@launch
            }
            repository.update(
                id,
                project.title,
                project.description,
                project.member,
                project.leader,
                project.createdAt
            )
        }
    }

    suspend fun deleteProject(id: Long, project: Project) {
        repository.delete(
            id,
            project.title,
            project.description,
            project.member,
            project.leader,
            project.createdAt
        )
    }
}