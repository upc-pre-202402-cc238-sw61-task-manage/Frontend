package com.task.management.workflow.profiles.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.management.workflow.common.Resource
import com.task.management.workflow.common.UIState
import com.task.management.workflow.iam.presentation.sign_in.SignInViewModel
import com.task.management.workflow.profiles.data.repository.ProfileRepository
import com.task.management.workflow.profiles.domain.model.Profile
import kotlinx.coroutines.launch

class ProfilesViewModel(val profilesRepository: ProfileRepository, val signInViewModel: SignInViewModel) : ViewModel() {
    private val _profile = mutableStateOf(UIState<Profile>())
    val profile: State<UIState<Profile>> get() = _profile

    init {
        viewModelScope.launch {
            getProfile()
        }
    }

    suspend fun getProfile() {
            val response = profilesRepository.getProfile(1)
            Log.d("ProfilesViewModel", "Response: ${response.message}")
            _profile.value = when (response) {
                is Resource.Success -> UIState(data = response.data?.let {
                    Profile(
                        firstName = it.firstName,
                        lastName = it.lastName,
                        email = it.email,
                        phoneNumber = it.phoneNumber
                    )
                })
                else -> UIState(error = "Unexpected response type")
            }
        }
}