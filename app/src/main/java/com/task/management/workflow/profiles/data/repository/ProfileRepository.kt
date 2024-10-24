package com.task.management.workflow.profiles.data.repository

import com.task.management.workflow.common.Resource
import com.task.management.workflow.profiles.data.remote.ProfileService
import com.task.management.workflow.profiles.data.remote.dataprofile.ProfileRequest
import com.task.management.workflow.profiles.data.remote.dataprofile.ProfileResponse
import com.task.management.workflow.profiles.domain.model.Profile
import retrofit2.Response

class ProfileRepository (private val service: ProfileService) {


    suspend fun DataProfile(profileRequest: ProfileRequest): Resource<ProfileResponse> {
        return try {
            val response = service.DataProfile(profileRequest)
            handleResponse(response)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun updateProfile(profile: Profile) {
        val request = ProfileRequest(
            name = profile.name,
            company = profile.company
        )
        service.updateProfile(request)
    }
    private fun <T> handleResponse(response: Response<T>): Resource<T> {
        return if (response.isSuccessful) {
            Resource.Success(response.body()!!)
        } else {
            Resource.Error(response.message())
        }
    }
}
