package com.task.management.workflow.project.data.repository

import com.task.management.workflow.common.Resource
import com.task.management.workflow.project.data.local.ProjectDao
import com.task.management.workflow.project.data.local.ProjectEntity
import com.task.management.workflow.project.data.local.toProject
import com.task.management.workflow.project.domain.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalProjectRepository (private val dao: ProjectDao) {

    suspend fun getProjects(): Resource<List<Project>> = withContext(Dispatchers.IO) {
        try {
            val response = dao.getProjects()
            if (response.isNotEmpty()) {
                response.let { projectsEntities: List<ProjectEntity> ->
                    val projects = projectsEntities.map { projectEntity: ProjectEntity ->
                        projectEntity.toProject()
                    }.toList()
                    return@withContext Resource.Success(data = projects)
                }
            }
            return@withContext Resource.Error(message = "An error occurred")
        } catch (e: Exception){
            return@withContext Resource.Error(message = e.message?: "An error occurred")
        }
    }

    suspend fun insertProject(title: String, description: String, leader: String) = withContext(Dispatchers.IO) {
        dao.insertProject(
            ProjectEntity(
                title = title,
                description = description,
                projectLeader = leader,
            )
        )
    }

    suspend fun deleteProject(title: String, description: String, leader: String) = withContext(Dispatchers.IO) {
        dao.deleteProject(
            ProjectEntity(
                title = title,
                description = description,
                projectLeader = leader
            )
        )
    }

    suspend fun updateProject(title: String, description: String, leader: String) = withContext(Dispatchers.IO) {
        dao.updateProject(
            ProjectEntity(
                title = title,
                description = description,
                projectLeader = leader
            )
        )
    }
}