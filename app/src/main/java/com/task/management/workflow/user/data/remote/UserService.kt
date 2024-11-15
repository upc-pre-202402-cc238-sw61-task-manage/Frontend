package com.task.management.workflow.user.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("api/v1/users")
    suspend fun getAllUsers(): Response<List<UserDto>>

    @GET("api/v1/users/{userId}")
    suspend fun getUserById(@Path("userId") userId: Long): Response<UserDto>
}