package com.task.management.workflow.iam.domain

data class User(
    val id: Long,
    val username: String,
    val password: String = "",
    val token: String,
)

