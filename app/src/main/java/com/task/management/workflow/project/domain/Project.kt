package com.task.management.workflow.project.domain

data class Project (
    var projectId: Long? = null,
    val title: String,
    val description: String,
    val leader: String
)