package com.task.management.workflow.projectUser.data.repository

import com.task.management.workflow.common.Resource
import com.task.management.workflow.iam.domain.model.User
import com.task.management.workflow.project.domain.Project
import com.task.management.workflow.projectUser.data.remote.ProjectUserRequest
import com.task.management.workflow.projectUser.data.remote.ProjectUserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProjectUserRepository(private val projectUserService: ProjectUserService) {

    suspend fun getUsersByProjectId(projectId: Long): Resource<List<User>> = withContext(Dispatchers.IO) {
        try {
            val response = projectUserService.getProjectUsers(projectId)
            if (response.isSuccessful) {
                return@withContext Resource.Success(data = response.body() ?: emptyList())
            } else {
                return@withContext Resource.Error("Couldn't fetch the project's users")
            }
        } catch (e: Exception) {
            return@withContext Resource.Error(message = e.message ?: "An error occurred")
        }
    }

    suspend fun addUserToProject(projectId: Long, userId: Long): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = projectUserService.addUserToProject(ProjectUserRequest(projectId, userId))
            if (response.isSuccessful) {
                return@withContext Resource.Success(Unit)
            } else {
                return@withContext Resource.Error("Couldn't add a user to the project")
            }
        } catch (e: Exception) {
            return@withContext Resource.Error(message = e.message ?: "Error occurred")
        }
    }

    suspend fun removeUserFromProject(projectId: Long, userId: Long): Resource<Unit> = withContext(Dispatchers.IO) {
        if(projectId == 0.toLong() || userId == 0.toLong()) return@withContext Resource.Error("Id cannot be null")
        try {
            val response = projectUserService.removeUserFromProject(ProjectUserRequest(projectId, userId))
            if (response.isSuccessful) {
                return@withContext Resource.Success(Unit)
            } else {
                return@withContext Resource.Error("Failed to delete project user: ${response.message()}")
            }
        } catch (e: Exception) {
            return@withContext Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun getProjectsByUserId(userId: Long): Resource<List<Project>> = withContext(Dispatchers.IO)  {
        try {
            val response = projectUserService.getUserProjects(userId)
            if (response.isSuccessful) {
                return@withContext Resource.Success(data = response.body() ?: emptyList())
            } else {
                return@withContext Resource.Error("Failed to fetch user projects")
            }
        } catch (e: Exception) {
            return@withContext Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun deleteUsersByProjectId(projectId: Long): Resource<Unit> = withContext(Dispatchers.IO) {
        if (projectId == 0L) return@withContext Resource.Error("Project ID cannot be null")
        try {
            val response = projectUserService.deleteUsersByProjectId(projectId)
            if (response.isSuccessful) {
                return@withContext Resource.Success(Unit)
            } else {
                return@withContext Resource.Error("Failed to delete users for project: ${response.message()}")
            }
        } catch (e: Exception) {
            return@withContext Resource.Error(e.message ?: "An error occurred")
        }
    }
}