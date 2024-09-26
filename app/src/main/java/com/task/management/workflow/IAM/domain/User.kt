package com.task.management.workflow.IAM.domain

data class User(
    val id: Long,
    val username: String,
    val password: String,
    val token: String,
)
