package com.task.management.workflow.profiles.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.management.workflow.common.Resource
import com.task.management.workflow.common.UIState
import com.task.management.workflow.common.session.UserSession
import com.task.management.workflow.iam.presentation.sign_in.SignInViewModel
import com.task.management.workflow.profiles.data.repository.ProfileRepository
import com.task.management.workflow.profiles.domain.model.Profile
import kotlinx.coroutines.launch

class ProfilesViewModel(val profilesRepository: ProfileRepository, val signInViewModel: SignInViewModel) : ViewModel() {
    private val _profile = mutableStateOf(UIState<Profile>())
    val profile: State<UIState<Profile>> get() = _profile

    private val _userId = mutableStateOf(UserSession.userId.value ?: 0)
    val userId: State<Long> get() = _userId

    init {
        viewModelScope.launch {
            getProfile()
        }
    }

    suspend fun updateProfile(profile: Profile) {
        try {
            val response = profilesRepository.updateProfile(profile)
            Log.d("ProfilesViewModel", "Response: $response")
            _profile.value = UIState(data = profile)
        } catch (e: Exception) {
            Log.e("ProfilesViewModel", "Error updating profile", e)
        }
    }

    suspend fun getProfile() {
        val response = profilesRepository.getProfile(userId.value)
        Log.d("ProfilesViewModel", "Response: ${response.message}")
        _profile.value = when (response) {
            is Resource.Success -> UIState(data = response.data?.let {
                Profile(
                    firstName = it.firstName,
                    lastName = it.lastName,
                    email = it.email,
                    phoneNumber = it.phoneNumber,
                    profilePhoto = it.profilePhoto
                )
            })
            else -> UIState(error = "Unexpected response type")
        }
    }
}