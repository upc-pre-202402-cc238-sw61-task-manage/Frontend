package com.task.management.workflow.IAM.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.management.workflow.IAM.data.remote.signin.SignInRequest
import com.task.management.workflow.IAM.data.repository.IAMRepository
import com.task.management.workflow.IAM.domain.User
import com.task.management.workflow.common.Resource
import com.task.management.workflow.common.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class IAMViewModel (private val repository: IAMRepository): ViewModel() {

    private val _user = mutableStateOf(UIState<User>())
    val user: State<UIState<User>> get() = _user

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> get() = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> get() = _password

    fun onUsernameChanged(username: String) {
        _username.value = username
    }

    fun onPasswordChanged(password: String) {
        _password.value = password
    }

    fun signIn() {
        _user.value = UIState(isLoading = true)
        viewModelScope.launch {
            val username = _username.value
            val password = _password.value
            val userRequest = SignInRequest(username, password)
            val response = repository.signIn(userRequest)

            if (response is Resource.Success) {
                val user = response.data?.let {
                    User(
                        id = it.id,
                        username = response.data.username,
                        password = password,
                        token = response.data.token
                    )
                }
                _user.value = UIState(data = user)
            } else {
                _user.value = UIState(error = response.message?:"An error occurred")
            }
        }
    }

}