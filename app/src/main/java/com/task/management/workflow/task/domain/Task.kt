package com.task.management.workflow.task.domain

data class Task(
    var name: String,
    var description: String,
    var dueDate: String,
    val userID: Long,
    val projectID: Long
)
