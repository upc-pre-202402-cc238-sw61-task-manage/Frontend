package com.task.management.workflow.project.data.repository

import com.task.management.workflow.project.domain.Project
import com.task.management.workflow.project.data.local.ProjectDao
import com.task.management.workflow.project.data.local.ProjectEntity
import com.task.management.workflow.project.data.remote.ProjectDto
import com.task.management.workflow.project.data.remote.ProjectService
import com.task.management.workflow.project.data.remote.toProject
import com.task.management.workflow.common.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProjectRepository(
    private val projectService: ProjectService,
    private val projectDao: ProjectDao
) {
    suspend fun insert(projectId: Long, name: String, description: String, member: String, leader: String) =
        withContext(Dispatchers.IO) {
            projectDao.insertProject(ProjectEntity(projectId, name, description, member, leader))
        }

    suspend fun delete(projectId: Long, name: String, description: String, member: String, leader: String) =
        withContext(Dispatchers.IO) {
            projectDao.deleteProject(ProjectEntity(projectId, name, description, member, leader))
        }

    suspend fun update(projectId: Long, name: String, description: String, member: String, leader: String) =
        withContext(Dispatchers.IO) {
            projectDao.updateProject(ProjectEntity(projectId, name, description, member, leader))
        }

    suspend fun getProjects(): Resource<List<Project>> = withContext(Dispatchers.IO) {
        val response = projectService.getAllProjects()
        if (response.isSuccessful) {
            response.body()?.projects?.let { projectsDto ->
                val projects = mutableListOf<Project>()
                projectsDto.forEach { projectDto: ProjectDto ->
                    val project = projectDto.toProject()
                    projects += project
                }
                return@withContext Resource.Success(data = projects.toList())
            }
            return@withContext Resource.Error(message = response.body()?.error ?: "")
        }
        return@withContext Resource.Error(message = "Data not found")
    }

}