package com.task.management.workflow.task.data.repository

import com.task.management.workflow.common.Resource
import com.task.management.workflow.task.data.remote.TaskDto
import com.task.management.workflow.task.data.remote.TaskService
import com.task.management.workflow.task.data.remote.toTask
import com.task.management.workflow.task.data.remote.toTaskDto
import com.task.management.workflow.task.domain.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteTaskRepository (private val service: TaskService) {
    suspend fun getTasksByProjectAndStatusRemotely(projectId: Long, status: String?): Resource<List<Task>> = withContext(
        Dispatchers.IO) {
        try {
            val response = service.getTasksByProjectId(projectId, status)
            if (response.isSuccessful) {
                response.body()?.let { tasksDto: List<TaskDto> ->
                    val tasks = tasksDto.map { taskDto: TaskDto ->
                        taskDto.toTask()
                    }.toList()
                    return@withContext Resource.Success(data = tasks)
                }
            }
            return@withContext Resource.Error(message = "An error occurred")
        } catch (e: Exception) {
            return@withContext Resource.Error(message = e.message ?: "An error occurred")
        }
    }

    suspend fun getTasksByProjectAndUserAndStatusRemotely(projectId: Long, userId: Long, status: String?): Resource<List<Task>> = withContext(
        Dispatchers.IO) {
        try {
            val response = service.getTasksByProjectAndUserId(projectId, userId, status)
            if (response.isSuccessful) {
                response.body()?.let { tasksDto: List<TaskDto> ->
                    val tasks = tasksDto.map { taskDto: TaskDto ->
                        taskDto.toTask()
                    }.toList()
                    return@withContext Resource.Success(data = tasks)
                }
            }
            return@withContext Resource.Error(message = "An error occurred")
        } catch (e: Exception) {
            return@withContext Resource.Error(message = e.message ?: "An error occurred")
        }
    }

    suspend fun insertRemotely(task: Task): Resource<Task> = withContext(Dispatchers.IO) {
        try {
            val newTaskDto = task.toTaskDto()
            val response = service.postTask(newTaskDto)
            if (response.isSuccessful) {
                response.body()?.let { dto ->
                    return@withContext Resource.Success(data = dto.toTask())
                }
            }
            return@withContext Resource.Error(message = "Error occurred")
        } catch (e: Exception) {
            return@withContext Resource.Error(message = e.message ?: "Error occurred")
        }
    }

    suspend fun updateRemotely(task: Task): Resource<Task> = withContext(Dispatchers.IO){
        val taskId = task.taskId ?: return@withContext Resource.Error("Id cannot be null")
        try {
            val response = service.updateTask(taskId, task.toTaskDto())
            if (response.isSuccessful) {
                response.body()?.let { taskDto ->
                    return@withContext Resource.Success(taskDto.toTask())
                }
            }
            return@withContext Resource.Error("Update failed")
        } catch (e: Exception) {
            return@withContext Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun deleteRemotely(taskId: Long): Resource<Unit> = withContext(Dispatchers.IO){
        if(taskId == 0.toLong()) return@withContext Resource.Error("An error occurred")
        try {
            val response = service.deleteTask(taskId)
            if (response.isSuccessful) {
                return@withContext Resource.Success(Unit)
            } else {
                return@withContext Resource.Error("Failed to delete task: ${response.message()}")
            }
        } catch (e: Exception) {
            return@withContext Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun getTasksByUserIdRemotely(taskId: Int): Resource<List<Task>> = withContext(
        Dispatchers.IO) {
        try {
            val response = service.getTasksByUserId(taskId)
            if (response.isSuccessful) {
                response.body()?.let { tasksDto: List<TaskDto> ->
                    val tasks = tasksDto.map { taskDto: TaskDto ->
                        taskDto.toTask()
                    }.toList()
                    return@withContext Resource.Success(data = tasks)
                }
            }
            return@withContext Resource.Error(message = "An error occurred")
        } catch (e: Exception) {
            return@withContext Resource.Error(message = e.message ?: "An error occurred")
        }
    }
}