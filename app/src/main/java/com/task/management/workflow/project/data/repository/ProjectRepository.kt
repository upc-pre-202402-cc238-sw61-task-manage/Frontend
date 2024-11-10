package com.task.management.workflow.project.data.repository

import com.task.management.workflow.project.domain.Project

class ProjectRepository(
    private val localRepository: LocalProjectRepository,
    private val remoteRepository: RemoteProjectRepository
) {
    //Local Repository
    suspend fun getLocally() = localRepository.getProjects()
    suspend fun insertLocally(title: String, description: String, leader: String) = localRepository.insertProject(title, description, leader)
    suspend fun updateLocally(title: String, description: String, leader: String) = localRepository.updateProject(title, description, leader)
    suspend fun deleteLocally(title: String, description: String, leader: String) = localRepository.deleteProject(title, description, leader)

    //Remote Repository
    suspend fun getAllRemotely() = remoteRepository.getAllProjects()
    suspend fun getOneRemotely(projectId: Long) = remoteRepository.getProject(projectId)
    suspend fun insertRemotely(project: Project) = remoteRepository.insertProject(project)
    suspend fun updateRemotely(project: Project) = remoteRepository.updateProject(project)
    suspend fun deleteRemotely(projectId: Long) = remoteRepository.deleteProject(projectId)
}