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
    suspend fun insert(projectId: Long, title: String, description: String, member: String, leader: String, createdAt: String) =
        withContext(Dispatchers.IO) {
            projectDao.insertProject(ProjectEntity(projectId, title, description, member, leader, createdAt))
        }

    suspend fun delete(projectId: Long, title: String, description: String, member: String, leader: String, createdAt: String) =
        withContext(Dispatchers.IO) {
            projectDao.deleteProject(ProjectEntity(projectId, title, description, member, leader, createdAt))
        }

    suspend fun update(projectId: Long, title: String, description: String, member: String, leader: String, createdAt: String) =
        withContext(Dispatchers.IO) {
            projectDao.updateProject(ProjectEntity(projectId, title, description, member, leader, createdAt))
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

    suspend fun getProjectFromDatabase(): List<Project> = withContext(Dispatchers.IO) {
        val projects = projectDao.getProjects()
        return@withContext projects.map { projectEntity: ProjectEntity ->
            Project(
                projectEntity.title,
                projectEntity.description,
                projectEntity.member,
                projectEntity.leader,
                projectEntity.createdAt
            )
        }
    }

}