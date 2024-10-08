package com.task.management.workflow.task.data.repository

import com.task.management.workflow.common.Resource
import com.task.management.workflow.task.data.local.TaskDao
import com.task.management.workflow.task.data.local.TaskEntity
import com.task.management.workflow.task.data.remote.TaskDto
import com.task.management.workflow.task.data.remote.TaskService
import com.task.management.workflow.task.data.remote.toTask
import com.task.management.workflow.task.domain.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class TaskRepository(
    private val taskService: TaskService,
    private val taskDao: TaskDao
) {
    suspend fun insertLocally(id: Long, task: Task) = withContext(Dispatchers.IO){
        taskDao.insert(
            TaskEntity(
                id,
                task.name,
                task.description,
                task.dueDate,
                task.userId,
                task.projectId
            )
        )
    }
    suspend fun insertRemote(task: Task): Response<TaskDto>{
        return taskService.postTask(task)
    }

    suspend fun deleteLocally(id: Long, task: Task) = withContext(Dispatchers.IO) {
        taskDao.delete(
            TaskEntity(
                id,
                task.name,
                task.description,
                task.dueDate,
                task.userId,
                task.projectId
            )
        )
    }

    suspend fun deleteRemote(taskId: Long): Response<Unit>{
        return taskService.deleteTask(taskId)
    }

    suspend fun updateLocally(id: Long, task: Task) = withContext(Dispatchers.IO) {
        taskDao.update(
            TaskEntity(
                id,
                task.name,
                task.description,
                task.dueDate,
                task.userId,
                task.projectId
            )
        )
    }

    suspend fun updateRemote(taskId: Long, task: Task): Response<TaskDto> {
        return taskService.updateTask(taskId,task)
    }

    suspend fun getTasks(): Resource<List<Task>> = withContext(Dispatchers.IO) {
        val response = taskService.getAllTasks()
        if(response.isSuccessful){
            response.body()?.tasks?.let { tasksDto ->
                val tasks = mutableListOf<Task>()
                tasksDto.forEach { taskDto: TaskDto ->
                    val task = taskDto.toTask()
                    tasks += task
                }
                return@withContext Resource.Success(data = tasks.toList())
            }
            return@withContext Resource.Error(message = response.body()?.error?: "")
        }
        return@withContext Resource.Error(message = "Data not found")
    }
}