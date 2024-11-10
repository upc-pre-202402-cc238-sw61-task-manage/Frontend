package com.task.management.workflow.projectUser.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.management.workflow.common.Resource
import com.task.management.workflow.common.UIState
import com.task.management.workflow.iam.domain.model.User
import com.task.management.workflow.projectUser.data.remote.ProjectUserRequest
import com.task.management.workflow.projectUser.data.repository.ProjectUserRepository
import kotlinx.coroutines.launch

class ProjectUserViewModel(private val projectUserRepository: ProjectUserRepository) : ViewModel() {

    private val _state = mutableStateOf(UIState<List<ProjectUserRequest>>())
    val state: State<UIState<List<ProjectUserRequest>>> get() = _state

    private val _projectUsers = mutableStateOf<List<User>>(emptyList())
    val projectUsers: State<List<User>> get() = _projectUsers

    fun getUsersByProjectId(projectId: Long) {
        viewModelScope.launch {
            val result = projectUserRepository.getUsersByProjectId(projectId)
            if (result is Resource.Success) {
                _projectUsers.value = result.data ?: emptyList()
            } else {
                _projectUsers.value = emptyList()
            }
        }
    }

    fun addUserToProject(projectId: Long, userId: Long) {
        viewModelScope.launch {
            projectUserRepository.addUserToProject(projectId, userId)
            getUsersByProjectId(projectId)
        }
    }

    fun removeUserFromProject(projectId: Long, userId: Long) {
        viewModelScope.launch {
            projectUserRepository.removeUserFromProject(projectId, userId)
            getUsersByProjectId(projectId)
        }
    }
}
