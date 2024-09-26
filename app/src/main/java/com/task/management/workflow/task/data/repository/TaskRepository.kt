package com.task.management.workflow.task.data.repository

import com.task.management.workflow.task.data.local.TaskDao
import com.task.management.workflow.task.data.local.TaskEntity
import com.task.management.workflow.task.data.remote.TaskService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

class TaskRepository(
    private val taskService: TaskService,
    private val taskDao: TaskDao
) {
    suspend fun insert(taskId: Long, name: String, description: String, dueDate: Date, userId: Long, projectId: Long) = withContext(Dispatchers.IO){
        taskDao.insert(TaskEntity(taskId, name, description, dueDate, userId, projectId))
    }

    suspend fun delete(taskId: Long, name: String, description: String, dueDate: Date, userId: Long, projectId: Long)
    = withContext(Dispatchers.IO) {
        taskDao.delete(TaskEntity(taskId, name, description, dueDate, userId, projectId))
    }

    suspend fun update(taskId: Long, name: String, description: String, dueDate: Date, userId: Long, projectId: Long) = withContext(Dispatchers.IO) {
        taskDao.update(TaskEntity(taskId, name, description, dueDate, userId, projectId))
    }

}