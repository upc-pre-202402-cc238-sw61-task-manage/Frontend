// app/src/main/java/com/task/management/workflow/iam/data/repository/IAMRepository.kt
package com.task.management.workflow.iam.data.repository

import com.task.management.workflow.common.Resource
import com.task.management.workflow.iam.data.remote.IAMService
import com.task.management.workflow.iam.data.remote.signup.SignUpRequest
import com.task.management.workflow.iam.data.remote.signin.SignInRequest
import com.task.management.workflow.iam.data.remote.signin.SignInResponse
import com.task.management.workflow.iam.data.remote.signup.SignUpResponse
import com.task.management.workflow.iam.data.remote.TokenProvider
import retrofit2.Response

class IAMRepository(private val service: IAMService, private val tokenProvider: TokenProvider) {

    suspend fun signIn(signInRequest: SignInRequest): Resource<SignInResponse> {
        return try {
            val response = service.signIn(signInRequest)
            handleResponse(response)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun signUp(signUpRequest: SignUpRequest): Resource<SignUpResponse> {
        return try {
            val response = service.signUp(signUpRequest)
            handleResponse(response)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    private fun <T> handleResponse(response: Response<T>): Resource<T> {
        return if (response.isSuccessful) {
            Resource.Success(response.body()!!)
        } else {
            Resource.Error(response.message())
        }
    }
}