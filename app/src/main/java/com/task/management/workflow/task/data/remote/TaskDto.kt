package com.task.management.workflow.task.data.remote

import com.google.gson.annotations.SerializedName
import com.task.management.workflow.task.domain.TaskStatus

data class TaskDto(
    @SerializedName("id")
    val taskId: Long? = 0,
    @SerializedName("taskName")
    var name: String,
    @SerializedName("taskDescription")
    var description: String,
    var dueDate: String,
    val projectId: Long,
    @SerializedName("assignUser")
    var userId: Long,
    var status: TaskStatus
)
