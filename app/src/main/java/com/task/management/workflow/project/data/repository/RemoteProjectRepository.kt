package com.task.management.workflow.project.data.repository

import com.task.management.workflow.common.Resource
import com.task.management.workflow.project.data.remote.ProjectDto
import com.task.management.workflow.project.data.remote.ProjectService
import com.task.management.workflow.project.data.remote.toProject
import com.task.management.workflow.project.domain.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteProjectRepository (private val service: ProjectService) {
    suspend fun getAllProjects(): Resource<List<Project>> = withContext(Dispatchers.IO) {
        try {
            val response = service.getAllProjects()
            if(response.isSuccessful){
                response.body()?.let{ projectsDto: List<ProjectDto> ->
                    val projects = projectsDto.map { projectDto: ProjectDto ->
                        projectDto.toProject()
                    }.toList()
                    return@withContext Resource.Success(data = projects)
                }
            }
            return@withContext Resource.Error(message = "An error occurred")
        } catch (e: Exception){
            return@withContext Resource.Error(message = e.message?: "An error occurred")
        }
    }

    suspend fun getProject(projectId: Long): Resource<Project> = withContext(Dispatchers.IO){
        if (projectId == 0.toLong()) return@withContext Resource.Error("An error occurred")
        try {
            val response = service.getProjectById(projectId)
            if(response.isSuccessful){
                val project = response.body()?.toProject()
                return@withContext Resource.Success(data = project!!)
            }
            return@withContext Resource.Error(message = "Couldn't retrieve project. Try again")
        } catch (e: Exception){
            return@withContext Resource.Error(message = e.message?: "An error occurred")
        }

    }

    suspend fun insertProject(project: Project): Resource<Project> = withContext(Dispatchers.IO){
        try {
            val newProjectDto = project.toProjectDto()
            val response = service.postProject(newProjectDto)
            if(response.isSuccessful){
                response.body()?.let { projectDto ->
                    return@withContext Resource.Success(data = projectDto.toProject() )
                }
            }
            return@withContext Resource.Error(message = "Error occurred")
        } catch (e: Exception){
            return@withContext Resource.Error(message = e.message ?: "Error occurred")
        }
    }

    suspend fun updateProject(project: Project): Resource<Project> = withContext(Dispatchers.IO){
        val projectId = project.projectId ?: return@withContext Resource.Error("Id cannot be null")
        try {
            val response = service.updateProject(projectId, project.toProjectDto())
            if (response.isSuccessful){
                response.body()?.let { projectDto ->
                    return@withContext Resource.Success(projectDto.toProject())
                }
            }
            return@withContext Resource.Error(message = "Update failed")
        } catch (e: Exception){
            return@withContext Resource.Error(message = e.message ?: "Error occurred")
        }
    }

    suspend fun deleteProject(projectId: Long): Resource<Unit> = withContext(Dispatchers.IO){
        if (projectId == 0.toLong()) return@withContext Resource.Error("An error occurred")
        try {
            val response = service.deleteProject(projectId)
            if(response.isSuccessful){
                return@withContext Resource.Success(Unit)
            } else {
                return@withContext Resource.Error("Failed to delete project")
            }
        } catch (e: Exception) {
            return@withContext Resource.Error(e.message ?: "An error occurred")
        }
    }
}