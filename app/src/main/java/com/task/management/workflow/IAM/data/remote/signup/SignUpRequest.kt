package com.task.management.workflow.IAM.data.remote.signup

data class SignUpRequest( 
    val username: String,
    val password: String,
    val roles: List<String>,
)