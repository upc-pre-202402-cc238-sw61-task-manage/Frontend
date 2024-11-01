package com.task.management.workflow.calendar.domain

import java.time.LocalDate

class CreateEventRequest(
    val projectId: Int,
    val userId: Int,
    val title: String,
    val description: String,
    var dueDate: String
) {
}