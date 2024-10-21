package com.task.management.workflow.task.data.repository

import com.task.management.workflow.common.Resource
import com.task.management.workflow.task.data.local.TaskDao
import com.task.management.workflow.task.data.local.TaskEntity
import com.task.management.workflow.task.data.remote.TaskDto
import com.task.management.workflow.task.data.remote.TaskService
import com.task.management.workflow.task.data.remote.toTask
import com.task.management.workflow.task.domain.Task
import com.task.management.workflow.task.domain.TaskStatus
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

    suspend fun deleteTask(taskId: Long, task: Task) = withContext(Dispatchers.IO){
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
}