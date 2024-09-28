package com.task.management.workflow.iam.domain.model

data class User(
    val id: Long,
    val username: String,
    val password: String = "",
    val token: String,
)

