package com.task.management.workflow.task.domain

import java.util.Date

data class Task(
    var name: String,
    var description: String,
    var dueDate: Date,
    val userID: Long,
    val projectID: Long
)
