package com.task.management.workflow.task.data.repository

import com.task.management.workflow.common.Resource
import com.task.management.workflow.task.data.local.TaskDao
import com.task.management.workflow.task.data.local.TaskEntity
import com.task.management.workflow.task.data.remote.TaskDto
import com.task.management.workflow.task.data.remote.toTask
import com.task.management.workflow.task.domain.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalTaskRepository (private val dao: TaskDao) {
    suspend fun insertLocally(taskId: Long, task: Task){
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

    suspend fun deleteLocally(taskId: Long, task: Task){
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

    /*
    suspend fun getAllLocally(): Resource<List<Task>>{
        return dao.fetchAll()
    }*/
}