package com.task.management.workflow.task.data.remote


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface TaskService {
    @Headers("Accept: application/json")
    @GET("/")
    suspend fun getTask(): Response<TaskDto>
}