package com.task.management.workflow.task.data.repository

import com.task.management.workflow.common.Resource
import com.task.management.workflow.task.data.local.TaskDao
import com.task.management.workflow.task.data.local.TaskEntity
import com.task.management.workflow.task.data.remote.TaskDto
import com.task.management.workflow.task.data.remote.TaskService
import com.task.management.workflow.task.data.remote.toTask
import com.task.management.workflow.task.data.remote.toTaskDto
import com.task.management.workflow.task.domain.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskRepository (
    private val service: TaskService,
    private val dao: TaskDao
){
    suspend fun insertTask(taskId: Long, task: Task) = withContext(Dispatchers.IO){
        dao.insert(
            TaskEntity(
                taskId,
                task.name,
                task.description,
                task.dueDate,
                task.userId,
                task.projectId,
                task.status
            )
        )
    }

    suspend fun eliminateTask(taskId: Long, task: Task) = withContext(Dispatchers.IO){
        dao.delete(
            TaskEntity(
                taskId,
                task.name,
                task.description,
                task.dueDate,
                task.userId,
                task.projectId,
                task.status
            )
        )
    }

    suspend fun getTasks(): Resource<List<Task>> = withContext(Dispatchers.IO){
        try {
            val response = service.getAllTasks()
            if(response.isSuccessful){
                response.body()?.let { tasksDto: List<TaskDto> ->
                    val tasks = tasksDto.map { taskDto: TaskDto ->
                        taskDto.toTask()
                    }.toList()
                    return@withContext Resource.Success(data = tasks)
                }
            }
            return@withContext Resource.Error(message = "An error occurred")
        } catch (e: Exception){
            return@withContext Resource.Error(message = e.message?: "An error occurred")
        }
    }

    suspend fun getTasksByProjectAndStatus(projectId: Long, status: String?): Resource<List<Task>> = withContext(Dispatchers.IO) {
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

    suspend fun getTasksByProjectAndUserAndStatus(projectId: Long, userId: Long, status: String?): Resource<List<Task>> = withContext(Dispatchers.IO) {
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

    suspend fun postTask(task: Task): Resource<Task> = withContext(Dispatchers.IO) {
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

    suspend fun putTask(task: Task): Resource<Task> = withContext(Dispatchers.IO){
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

    suspend fun deleteTask(taskId: Long): Resource<Unit> = withContext(Dispatchers.IO){
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

}