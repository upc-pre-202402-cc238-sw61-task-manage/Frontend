package com.task.management.workflow.calendar.data.remote

import com.task.management.workflow.calendar.domain.EventPackage

data class PackageDto(
    val id: Int,
    val projectId: Int,
    val userId: Int,
    val title: String,
    val description: String,
    val day: Int,
    val month: Int,
    val year: Int
)
fun PackageDto.toPackage(): EventPackage = EventPackage(id, projectId, userId, title, description, day, month, year)
