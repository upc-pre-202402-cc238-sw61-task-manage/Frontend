package com.task.management.workflow.calendar.data.remote

import com.task.management.workflow.calendar.domain.EventPackage
import java.time.LocalDate

data class PackageDto(
    val id: Int,
    val projectId: Int,
    val userId: Int,
    val title: String,
    val description: String,
    var dueDate: String
)
fun PackageDto.toPackage(): EventPackage = EventPackage(id, projectId, userId, title, description, dueDate)
