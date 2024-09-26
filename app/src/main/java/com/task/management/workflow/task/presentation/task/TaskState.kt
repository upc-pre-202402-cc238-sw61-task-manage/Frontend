package com.task.management.workflow.task.presentation.task

import com.task.management.workflow.task.domain.Task
import java.util.Date

data class TaskState(
    val isLoading: Boolean = false,
    val task: Task? = null,
    val name: String = "",
    val description: String = "",
    var dueDate: Date? = null
)
