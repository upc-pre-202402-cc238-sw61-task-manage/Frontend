package com.task.management.workflow.iam.data.remote.signin

data class SignInResponse(
    val id: Long,
    val username: String,
    val token: String,
)
