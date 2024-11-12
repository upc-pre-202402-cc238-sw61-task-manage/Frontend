package com.task.management.workflow.calendar.data.remote

import com.task.management.workflow.calendar.domain.EventPackage

data class CalendarDto(
    val id: Int,
    val projectId: Int,
    val userId: Int,
    val title: String,
    val description: String,
    var dueDate: String
)
fun CalendarDto.toPackage(): EventPackage = EventPackage(id, projectId, userId, title, description, dueDate)
