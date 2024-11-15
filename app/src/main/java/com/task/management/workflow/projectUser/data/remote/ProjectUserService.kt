package com.task.management.workflow.projectUser.data.remote

import com.task.management.workflow.user.domain.User
import com.task.management.workflow.project.data.remote.ProjectDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path

interface ProjectUserService {
    @GET("api/v1/project-users/project/{projectId}/users")
    suspend fun getProjectUsers(
        @Path("projectId") projectId: Long
    ): Response<List<User>>

    @GET("api/v1/project-users/user/{userId}/projects")
    suspend fun getUserProjects(
        @Path("userId") userId: Long
    ): Response<List<ProjectDto>>

    @POST("api/v1/project-users/add")
    suspend fun addUserToProject(
        @Body projectUserRequest: ProjectUserRequest
    ): Response<Void>

    @HTTP(method = "DELETE", path = "api/v1/project-users/remove", hasBody = true)
    suspend fun removeUserFromProject(
        @Body projectUserRequest: ProjectUserRequest
    ): Response<Void>

    @DELETE("api/v1/project-users/project/{projectId}/users")
    suspend fun deleteUsersByProjectId(
        @Path("projectId") projectId: Long
    ): Response<Void>

}