package com.task.management.workflow.project.data.remote

data class ProjectDto(
    val projectId: Long,
    val name: String,
    val description: String,
    val member: String,
    val leader: String
)