package com.task.management.workflow.iam.data.remote

data class UserResponse(
    val id: Long,
    val username: String,
    val roles: List<String>,
)
