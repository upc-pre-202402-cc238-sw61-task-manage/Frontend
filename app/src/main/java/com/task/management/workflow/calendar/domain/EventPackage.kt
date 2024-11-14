package com.task.management.workflow.calendar.domain

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class EventPackage(
    val id: Long,
    val projectId: Long,
    val userId: Long,
    val title: String,
    val description: String,
    var dueDate: String)
{
    @RequiresApi(Build.VERSION_CODES.O)
    fun toLocalDate(): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return LocalDate.parse(dueDate, formatter)
    }

}
