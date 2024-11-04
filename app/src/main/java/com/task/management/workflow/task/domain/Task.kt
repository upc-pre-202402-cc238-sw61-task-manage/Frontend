package com.task.management.workflow.task.domain

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Task(
    var taskId: Long? = null,
    var name: String,
    var description: String,
    var dueDate: String,
    val userId: Long,
    val projectId: Long,
    var status: TaskStatus = TaskStatus.NEW
){
    @RequiresApi(Build.VERSION_CODES.O)
    fun toLocalDate(): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return LocalDate.parse(dueDate, formatter)
    }
}