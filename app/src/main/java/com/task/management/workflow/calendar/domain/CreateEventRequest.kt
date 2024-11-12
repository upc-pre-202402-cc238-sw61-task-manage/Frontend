package com.task.management.workflow.calendar.domain

class CreateEventRequest(
    val projectId: Long,
    val userId: Int,
    val title: String,
    val description: String,
    var dueDate: String
) {
}