package com.task.management.workflow.task.data.repository

import com.task.management.workflow.common.Resource
import com.task.management.workflow.task.data.local.TaskDao
import com.task.management.workflow.task.data.local.TaskEntity
import com.task.management.workflow.task.data.remote.TaskService
import com.task.management.workflow.task.data.remote.toTask
import com.task.management.workflow.task.domain.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskRepository(
    private val taskService: TaskService,
    private val taskDao: TaskDao
) {
    suspend fun insert(taskEntity: TaskEntity) = withContext(Dispatchers.IO){
        taskDao.insert(TaskEntity(
            taskEntity.taskId,
            taskEntity.name,
            taskEntity.description,
            taskEntity.dueDate,
            taskEntity.userID,
            taskEntity.projectID
        ))
    }

    suspend fun delete(taskEntity: TaskEntity) = withContext(Dispatchers.IO) {
        taskDao.delete(TaskEntity(
            taskEntity.taskId,
            taskEntity.name,
            taskEntity.description,
            taskEntity.dueDate,
            taskEntity.userID,
            taskEntity.projectID
        ))
    }

    suspend fun update(taskEntity: TaskEntity) = withContext(Dispatchers.IO) {
        taskDao.update(TaskEntity(
            taskEntity.taskId,
            taskEntity.name,
            taskEntity.description,
            taskEntity.dueDate,
            taskEntity.userID,
            taskEntity.projectID
        ))
    }

    suspend fun fetchAll() : Resource<Task> = withContext(Dispatchers.IO) {
        val response = taskService.getTask()
        if(response.isSuccessful) {
            response.body()?.let { taskDto ->
                val task = taskDto.toTask()
                taskDao.fetchByName(task.name)?.let { taskEntity ->
                    task.description = taskEntity.description
                }
                return@withContext Resource.Success(data = task)
            }
            return@withContext Resource.Error(message = response.message())
        }
        return@withContext Resource.Error(message = response.message())
    }

    /*
    suspend fun getJoke(): Resource<Joke> = withContext(Dispatchers.IO) {
        val response = service.getJoke()

        if (response.isSuccessful) {
            response.body()?.let { jokeDto ->
                val joke = jokeDto.toJoke()
                dao.fetchByDescription(joke.description)?.let { jokeEntity ->
                    joke.score = jokeEntity.score
                }

                return@withContext Resource.Success(data = joke)
            }
            return@withContext Resource.Error(message = response.message())
        }

        return@withContext Resource.Error(message = response.message())

    }

    suspend fun getJokes(): Resource<List<Joke>> = withContext(Dispatchers.IO) {

        try {
            val entities = dao.fetchAll()
            val jokes = entities.map { jokeEntity: JokeEntity ->
                Joke(jokeEntity.description, jokeEntity.score)
            }.toList()
            return@withContext Resource.Success(jokes)
        } catch (exception: Exception) {
            return@withContext Resource.Error(exception.message ?: "An error occurred")

        }

    }

    * */
}