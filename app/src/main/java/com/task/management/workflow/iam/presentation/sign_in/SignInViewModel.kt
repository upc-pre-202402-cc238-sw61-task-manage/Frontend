package com.task.management.workflow.iam.presentation.sign_in

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.management.workflow.common.Resource
import com.task.management.workflow.common.UIState
import com.task.management.workflow.common.session.UserSession
import com.task.management.workflow.iam.data.local.AccountEntity
import com.task.management.workflow.iam.data.remote.TokenProvider
import com.task.management.workflow.iam.data.remote.signin.SignInRequest
import com.task.management.workflow.iam.data.repository.IAMRepository
import com.task.management.workflow.user.domain.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignInViewModel(private val repository: IAMRepository, private val tokenProvider: TokenProvider) : ViewModel() {

    private val _userList = mutableStateOf(UIState<List<AccountEntity>>())
    val userList: State<UIState<List<AccountEntity>>> get() = _userList

    private val _user = mutableStateOf(UIState<User>())
    val user: State<UIState<User>> get() = _user

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> get() = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> get() = _password

    private val _jwtToken = MutableStateFlow("")
    val jwtToken: StateFlow<String> get() = _jwtToken

    init {
        getUsers()
    }

    fun getUsers() {
        _userList.value = UIState(isLoading = true)
        _user.value = UIState(isLoading = true)
        viewModelScope.launch {
            try {
                val response = repository.getAccounts()
                _userList.value = UIState(data = response)
                if (response.isNotEmpty()) {
                    tokenProvider.setToken(response[0].jwtToken)
                    val userResponse = repository.getUserById(response[0].accountId)
                    if (userResponse is Resource.Success) {
                        Log.d("SignInViewModel", "Doing fast sign in")
                        fastSignIn()
                    }
                    if (userResponse is Resource.Error) {
                        _user.value = UIState(error = userResponse.message ?: "Your last session has expired")
                    }
                }
            } catch (e: Exception) {
                _userList.value = UIState(error = e.message ?: "An error occurred")
                Log.e("SignInViewModel", "Error fetching users", e)
            }
        }
    }

    fun onUsernameChanged(username: String) {
        _username.value = username
    }

    fun onPasswordChanged(password: String) {
        _password.value = password
    }

    fun saveInDatabase() {
        viewModelScope.launch {
            val username = _username.value
            val password = _password.value
            val user = AccountEntity(username = username, password = password, jwtToken = jwtToken.value)
            repository.saveAccount(user)
        }
    }
    fun fastSignIn() {
        val user = _userList.value.data?.get(0)
        _user.value = UIState(data = User(id = user?.accountId ?: 0, username = user?.username ?: ""))
        tokenProvider.setToken(user?.jwtToken ?: "")
        UserSession.setUserId(user?.accountId ?: 0)
    }
    fun signIn() {
        _user.value = UIState(isLoading = true)
        viewModelScope.launch {
            val username = _username.value
            val password = _password.value
            val userRequest = SignInRequest(username, password)
            val response = repository.signIn(userRequest)

            if (response is Resource.Success) {
                response.data?.token?.let {
                    tokenProvider.setToken(it)
                    _jwtToken.value = it
                }
                val user = response.data?.let {
                    User(
                        id = it.id,
                        username = it.username
                    )
                }
                _user.value = UIState(data = user)
                user?.id?.let { UserSession.setUserId(it) }
                // LOG the current user in UserSession
                Log.d("SignInViewModel", "Signed in user: ${UserSession.getUserId().toString() ?: "No user"}")
            } else {
                _user.value = UIState(error = response.message ?: "An error occurred")
            }
        }
    }
}