package com.task.management.workflow.project.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProjectService {
    @GET("/api/v1/projects/{projectId}")
    suspend fun getProjectById(@Path("projectId") projectId: Long): Response<ProjectDto>

    @GET("/api/v1/projects")
    suspend fun getAllProjects(): Response<List<ProjectDto>>

    @POST("/api/v1/projects")
    suspend fun postProject(@Body project: ProjectDto): Response<ProjectDto>

    @PUT("/api/v1/projects/{projectId}")
    suspend fun updateProject(
        @Path("projectId") projectId: Long,
        @Body projectDto: ProjectDto
    ): Response<ProjectDto>

    @DELETE("/api/v1/projects/{projectId}")
    suspend fun deleteProject(@Path("projectId") projectId: Long) :Response<Unit>


}