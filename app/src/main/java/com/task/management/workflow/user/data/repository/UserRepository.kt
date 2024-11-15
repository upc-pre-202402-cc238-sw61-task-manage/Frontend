package com.task.management.workflow.user.data.repository

import com.task.management.workflow.common.Resource
import com.task.management.workflow.user.data.remote.UserDto
import com.task.management.workflow.user.data.remote.UserService
import com.task.management.workflow.user.domain.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository (private val service: UserService) {
    suspend fun getAllUsers(): Resource<List<User>> = withContext(Dispatchers.IO){
        try {
            val response = service.getAllUsers()
            if (response.isSuccessful){
                response.body()?.let { usersDto: List<UserDto> ->
                    val users = usersDto.map { userDto: UserDto ->
                        userDto.toUser()
                    }.toList()
                    return@withContext Resource.Success(data = users)
                }
            }
            return@withContext Resource.Error(message = "An error occurred")
        } catch (e: Exception){
            return@withContext Resource.Error(message = e.message ?: "An error occurred")
        }
    }

    suspend fun getUserById(userId: Long): Resource<User> = withContext(Dispatchers.IO){
        if (userId == 0.toLong()) return@withContext Resource.Error("An error occurred")
        try {
            val response = service.getUserById(userId)
            if(response.isSuccessful){
                val user = response.body()?.toUser()
                return@withContext Resource.Success(data = user!!)
            }
            return@withContext Resource.Error(message = "Could not retrieve user. Try again")
        } catch (e: Exception){
            return@withContext Resource.Error(message = e.message?: "An error occurred")
        }
    }
}