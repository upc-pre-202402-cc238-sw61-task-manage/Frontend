package com.task.management.workflow.profiles.data.remote

import com.task.management.workflow.profiles.data.remote.dataprofile.ProfileRequest
import com.task.management.workflow.profiles.data.remote.dataprofile.ProfileResponse
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ProfileService {
    @POST("api/v1/profiles/data")
    suspend fun DataProfile(@Body ProfileRequest: ProfileRequest): retrofit2.Response<ProfileResponse>
    abstract fun updateProfile(request: ProfileRequest)
    abstract fun getProfile(): Any
}