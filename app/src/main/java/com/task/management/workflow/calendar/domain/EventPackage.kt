package com.task.management.workflow.calendar.domain

data class EventPackage(
    val id: Int,
    val projectId: Int,
    val userId: Int,
    val title: String,
    val description: String,
    val day: Int,
    val month: Int,
    val year: Int)
{
    fun formattedDate(): String {
        val formattedDay = day.toString().padStart(2, '0')
        val formattedMonth = month.toString().padStart(2, '0')
        return "$formattedDay/$formattedMonth/$year"
    }

}
