package com.task.management.workflow.project.data.remote

data class ProjectResponseDto (
    val response: String,
    val projects: List<ProjectDto>?,
    val error: String?
)