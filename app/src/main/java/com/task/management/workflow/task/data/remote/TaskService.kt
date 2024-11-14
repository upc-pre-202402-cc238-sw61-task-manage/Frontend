package com.task.management.workflow.task.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TaskService {

    @GET("api/v1/tasks/{taskId}")
    suspend fun getTask(@Path("taskId") taskId: Long): Response<TaskDto>

    @GET("api/v1/tasks")
    suspend fun getAllTasks(): Response<List<TaskDto>>

    @GET("api/v1/tasks/project/{projectId}")
    suspend fun getTasksByProjectId(
        @Path("projectId") projectId: Long,
        @Query("status") status: String? = null
    ): Response<List<TaskDto>>

    @GET("api/v1/tasks/project/{projectId}/user/{userId}")
    suspend fun getTasksByProjectAndUserId(
        @Path("projectId") projectId: Long,
        @Path("userId") userId: Long,
        @Query("status") status: String? = null
    ): Response<List<TaskDto>>

    @POST("api/v1/tasks")
    suspend fun postTask(@Body task: TaskDto): Response<TaskDto>

    @PUT("api/v1/tasks/{taskId}")
    suspend fun updateTask(
        @Path("taskId") taskId: Long,
        @Body task: TaskDto
    ): Response<TaskDto>

    @DELETE("api/v1/tasks/{taskId}")
    suspend fun deleteTask(
        @Path("taskId") taskId: Long
    ): Response<Unit>

    //
    @GET("api/v1/tasks/user/{userId}")
    suspend fun getTasksByUserId(@Path("userId") userId: Long): Response<List<TaskDto>>
}