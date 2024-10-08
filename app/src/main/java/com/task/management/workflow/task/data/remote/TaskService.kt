package com.task.management.workflow.task.data.remote


import com.task.management.workflow.task.domain.Task
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskService {
    @GET("/api/v1/tasks/{taskId}/")
    suspend fun getTask(@Path("taskId")taskId: Long): Response<TaskDto>

    @GET("/api/v1/tasks/")
    suspend fun getAllTasks(): Response<TaskResponseDto>

    @POST("/api/v1/tasks/")
    suspend fun postTask(@Body task: Task): Response<TaskDto>

    @PUT("/api/v1/tasks/{taskId}/")
    suspend fun updateTask(
        @Path("taskId")taskId: Long,
        @Body task: Task
    ): Response<TaskDto>

    @DELETE("/api/v1/tasks/{taskId}/")
    suspend fun deleteTask(
        @Path("taskId")taskId: Long
    ): Response<Unit>

}