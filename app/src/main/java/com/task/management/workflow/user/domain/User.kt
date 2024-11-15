package com.task.management.workflow.user.domain

data class User(
    val id: Long,
    val username: String,
    val roles: Set<Roles> = emptySet()
)

