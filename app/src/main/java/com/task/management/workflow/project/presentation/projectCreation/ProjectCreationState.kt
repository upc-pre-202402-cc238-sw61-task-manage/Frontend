package com.task.management.workflow.project.presentation.projectCreation

import com.task.management.workflow.project.domain.Project

data class ProjectCreationState (
    val isLoading: Boolean = false,
    val projects: List<Project>? = null,
    val error: String? = ""
)