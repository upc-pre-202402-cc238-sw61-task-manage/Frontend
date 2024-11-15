package com.task.management.workflow.project.domain

import com.task.management.workflow.project.data.remote.ProjectDto

data class Project (
    var projectId: Long? = null,
    val title: String,
    val description: String,
    val leader: String
) {
    fun toProjectDto(): ProjectDto {
        return ProjectDto(projectId, title, description, leader)
    }
}