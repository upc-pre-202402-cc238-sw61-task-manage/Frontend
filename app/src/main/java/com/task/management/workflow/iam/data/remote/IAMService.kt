package com.task.management.workflow.iam.data.remote

import com.task.management.workflow.iam.data.remote.signin.SignInRequest
import com.task.management.workflow.iam.data.remote.signin.SignInResponse
import com.task.management.workflow.iam.data.remote.signup.SignUpRequest
import com.task.management.workflow.iam.data.remote.signup.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface IAMService {
    @POST("api/v1/authentication/sign-in")
    suspend fun signIn(@Body signInRequest: SignInRequest): Response<SignInResponse>

    @POST("api/v1/authentication/sign-up")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>

    @GET("api/v1/users/{id}")
    suspend fun getUserById(@Path("id") id: Long): Response<UserResponse>
}