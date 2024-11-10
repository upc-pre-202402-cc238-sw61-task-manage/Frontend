package com.task.management.workflow.task.presentation.taskFilter

data class TaskFilterOptions(
    val projectId: String,
    val userId: String,
    val status: String,
    val onlyShowUser: Boolean
)
