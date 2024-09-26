package com.task.management.workflow.task.data.remote

import com.google.gson.annotations.SerializedName
import java.util.Date

data class TaskDto(
    val taskId: Long,
    var name: String,
    var description: String,
    var dueDate: String,
    var userID: Long,
    val projectID: Long
)


