package com.task.management.workflow.iam.data.remote.signup

data class SignUpRequest(
    val username: String,
    val password: String,
    val roles: List<String>,
)