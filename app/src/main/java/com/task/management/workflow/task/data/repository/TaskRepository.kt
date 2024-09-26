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

class TaskRepository(
    private val taskService: TaskService,
    private val taskDao: TaskDao
) {
    suspend fun insert(taskId: Long, name: String, description: String, dueDate: String, userId: Long, projectId: Long) = withContext(Dispatchers.IO){
        taskDao.insert(TaskEntity(taskId, name, description, dueDate, userId, projectId))
    }

    suspend fun delete(taskId: Long, name: String, description: String, dueDate: String, userId: Long, projectId: Long) = withContext(Dispatchers.IO) {
        taskDao.delete(TaskEntity(taskId, name, description, dueDate, userId, projectId))
    }

    suspend fun update(taskId: Long, name: String, description: String, dueDate: String, userId: Long, projectId: Long) = withContext(Dispatchers.IO) {
        taskDao.update(TaskEntity(taskId, name, description, dueDate, userId, projectId))
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