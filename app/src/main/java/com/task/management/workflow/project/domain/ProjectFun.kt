package com.task.management.workflow.project.domain

import com.task.management.workflow.project.data.remote.ProjectDto

fun Project.toProjectDto(): ProjectDto {
    return ProjectDto(projectId, title, description, leader)
}