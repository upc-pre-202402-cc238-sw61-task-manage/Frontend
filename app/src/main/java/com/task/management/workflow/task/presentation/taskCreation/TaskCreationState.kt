package com.task.management.workflow.task.presentation.taskCreation

import com.task.management.workflow.task.domain.Task
import java.util.Date

data class TaskCreationState(
    val isLoading: Boolean = false,
    val tasks: List<Task>? = null,
    val name: String = "",
    val description: String = "",
    var dueDate: Date? = null
)
