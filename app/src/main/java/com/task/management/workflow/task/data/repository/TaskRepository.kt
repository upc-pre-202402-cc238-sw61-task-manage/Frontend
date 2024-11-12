package com.task.management.workflow.task.data.repository

import com.task.management.workflow.task.domain.Task

class TaskRepository (
    private val localTaskRepository: LocalTaskRepository,
    private val remoteTaskRepository: RemoteTaskRepository
){
    //Local Repository
    //suspend fun getLocally() = localTaskRepository.getLocally()
    suspend fun insertLocally(taskId: Long, task: Task) = localTaskRepository.insertLocally(taskId, task)
    suspend fun deleteLocally(taskId: Long, task: Task) = localTaskRepository.deleteLocally(taskId, task)

    //Remote Repository
    suspend fun insertRemotely(task: Task) = remoteTaskRepository.insertRemotely(task)
    suspend fun updateRemotely(task: Task) = remoteTaskRepository.updateRemotely(task)
    suspend fun deleteRemotely(taskId: Long) = remoteTaskRepository.deleteRemotely(taskId)
    suspend fun getTasksByProjectAndStatusRemotely(projectId: Long, status: String?) = remoteTaskRepository.getTasksByProjectAndStatusRemotely(projectId,status)
    suspend fun getTasksByProjectAndUserAndStatusRemotely(projectId: Long, userId: Long, status: String?) = remoteTaskRepository.getTasksByProjectAndUserAndStatusRemotely(projectId, userId,status)
    suspend fun getTasksByUserIdRemotely(taskId: Int) = remoteTaskRepository.getTasksByUserIdRemotely(taskId)
}