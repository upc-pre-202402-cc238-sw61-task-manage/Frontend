package com.task.management.workflow.profiles.data.remote

import com.task.management.workflow.profiles.data.remote.dataprofile.ProfileResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileService {
    @GET("api/v1/profiles/{profileId}")
    suspend fun getProfileById(@Path("profileId") profileId: Long): Response<ProfileResponse>

}