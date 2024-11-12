package com.task.management.workflow.profiles.data.repository

import com.task.management.workflow.common.Resource
import com.task.management.workflow.profiles.data.remote.ProfileService
import com.task.management.workflow.profiles.data.remote.dataprofile.ProfileRequest
import com.task.management.workflow.profiles.data.remote.dataprofile.ProfileResponse
import com.task.management.workflow.profiles.domain.model.Profile
import retrofit2.Response

class ProfileRepository(private val service: ProfileService) {

    suspend fun getProfile(profileId: Long): Resource<ProfileResponse> {
        return try {
            val response = service.getProfileById(profileId)
            handleResponse(response)
        } catch (e: Exception) {
            Resource.Error<ProfileResponse>(e.message ?: "An error occurred")
        }
    }

    suspend fun updateProfile(profile: Profile) {
        val request = ProfileRequest(
            profile.firstName,
            profile.lastName,
            profile.email,
            profile.phoneNumber,
            1
        )
        service.updateProfile(1, request)
    }

    private fun <T> handleResponse(response: Response<T>): Resource<T> {
        return if (response.isSuccessful) {
            Resource.Success(response.body()!!)
        } else {
            Resource.Error(response.message())
        }
    }
}