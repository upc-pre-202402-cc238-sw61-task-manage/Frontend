package com.task.management.workflow.user.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.management.workflow.common.Resource
import com.task.management.workflow.common.UIState
import com.task.management.workflow.user.data.repository.UserRepository
import com.task.management.workflow.user.domain.User
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _usersState = mutableStateOf(UIState<List<User>>())
    val usersState: State<UIState<List<User>>> get() = _usersState

    private val _selectedUserState = mutableStateOf(UIState<User?>())
    val selectedUserState: State<UIState<User?>> get() = _selectedUserState

    fun getAllUsers() {
        _usersState.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = userRepository.getAllUsers()
            if (result is Resource.Success) {
                _usersState.value = UIState(data = result.data)
            } else {
                _usersState.value = UIState(error = result.message ?: "An error occurred")
            }
        }
    }

    fun getUserById(userId: Long) {
        _selectedUserState.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = userRepository.getUserById(userId)
            if (result is Resource.Success) {
                _selectedUserState.value = UIState(data = result.data)
            } else {
                _selectedUserState.value = UIState(error = result.message ?: "An error occurred")
            }
        }
    }
}