package com.task.management.workflow.project.data.remote

import com.task.management.workflow.project.domain.Project
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProjectService {
    @GET("/api/v1/projects/{projectId}/")
    suspend fun getProject(@Path("projectId") projectId: Long): Response<ProjectDto>

    @GET("/api/v1/projects/")
    suspend fun getAllProjects(): Response<ProjectResponseDto>

    @POST("/api/v1/projects/")
    suspend fun postProject(project: ProjectDto)

    @PUT("/api/v1/projects/{projectId}/")
    suspend fun updateProject(@Path("projectId") projectId: Long, project: Project)

    @DELETE("/api/v1/projects/{projectId}/")
    suspend fun deleteProject(@Path("projectId") projectId: Long)


}