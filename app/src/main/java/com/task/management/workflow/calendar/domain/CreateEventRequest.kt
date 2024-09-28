package com.task.management.workflow.calendar.domain

class CreateEventRequest(
    val projectId: Int,
    val userId: Int,
    val title: String,
    val description: String,
    val day: Int,
    val month: Int,
    val year: Int
) {
}