package com.task.management.workflow.iam.data.repository

import com.task.management.workflow.iam.data.remote.IAMService
import com.task.management.workflow.common.Resource
import com.task.management.workflow.iam.data.remote.signin.SignInRequest
import com.task.management.workflow.iam.data.remote.signin.SignInResponse
import com.task.management.workflow.iam.data.remote.signup.SignUpRequest
import com.task.management.workflow.iam.data.remote.signup.SignUpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IAMRepository(private val service: IAMService) {

    suspend fun signIn(signInRequest: SignInRequest): Resource<SignInResponse> = withContext(Dispatchers.IO) {
        try {
            val response = service.signIn(signInRequest)
            if (response.isSuccessful) {
                response.body()?.let { user ->
                    return@withContext Resource.Success(user)
                } ?: return@withContext Resource.Error(message = response.message())
            } else {
                return@withContext Resource.Error(message = response.message())
            }
        } catch (e: Exception) {
            return@withContext Resource.Error(message = e.message ?: "An exception occurred")
        }
    }

    suspend fun signUp(signUpRequest: SignUpRequest): Resource<SignUpResponse> = withContext(Dispatchers.IO) {
        try {
            val response = service.signUp(signUpRequest)
            if (response.isSuccessful) {
                response.body()?.let { user ->
                    return@withContext Resource.Success(user)
                } ?: return@withContext Resource.Error(message = response.message())
            } else {
                return@withContext Resource.Error(message = response.message())
            }
        } catch (e: Exception) {
            return@withContext Resource.Error(message = e.message ?: "An exception occurred")
        }
    }
}