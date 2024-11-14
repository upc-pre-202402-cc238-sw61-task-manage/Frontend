package com.task.management.workflow.iam.presentation.sign_up

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.management.workflow.common.Resource
import com.task.management.workflow.common.UIState
import com.task.management.workflow.iam.data.remote.signup.SignUpRequest
import com.task.management.workflow.iam.data.repository.IAMRepository
import com.task.management.workflow.iam.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: IAMRepository) : ViewModel() {

    private val _user = mutableStateOf(UIState<User>())
    val user: State<UIState<User>> get() = _user

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> get() = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> get() = _password

    private val _roles = MutableStateFlow("")
    val roles: StateFlow<String> get() = _roles

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> get() = _errorMessage

    fun onUsernameChanged(username: String) {
        _username.value = username
    }

    fun onPasswordChanged(password: String) {
        _password.value = password
    }

    fun onRolesChanged(roles: String) {
        _roles.value = roles
    }

    fun signUp() {
        if (_username.value.isEmpty() || _password.value.isEmpty() || _roles.value.isEmpty()) {
            _errorMessage.value = "All fields must be filled"
            return
        }

        _user.value = UIState(isLoading = true)
        viewModelScope.launch {
            val username = _username.value
            val password = _password.value
            val roles = _roles.value.split(",").map { it.trim() }
            val userRequest = SignUpRequest(username, password, roles)
            val response = repository.signUp(userRequest)

            if (response is Resource.Success) {
                val user = response.data?.let {
                    User(
                        id = it.id,
                        username = it.username,
                        password = password,
                        token = ""
                    )
                }
                _user.value = UIState(data = user)
            } else {
                _errorMessage.value = response.message ?: "An error occurred"
                _user.value = UIState(error = response.message ?: "An error occurred")
            }
        }
    }
}