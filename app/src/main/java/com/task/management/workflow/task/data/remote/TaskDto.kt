package com.task.management.workflow.task.data.remote

import com.google.gson.annotations.SerializedName

data class TaskDto(
    val taskId: Long,
    @SerializedName("taskName")
    var name: String,
    @SerializedName("taskDescription")
    var description: String,
    var dueDate: String,
    val projectId: Long,
    @SerializedName("assignedUser")
    var userId: Long
)
