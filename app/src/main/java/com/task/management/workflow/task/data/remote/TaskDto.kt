package com.task.management.workflow.task.data.remote

import com.google.gson.annotations.SerializedName
import java.util.Date

data class TaskDto(
    @SerializedName("task_id")
    val taskId: Long,
    @SerializedName("task_name")
    var name: String,
    @SerializedName("task_description")
    var description: String,
    @SerializedName("due_date")
    var dueDate: String,
    @SerializedName("user_id")
    var userID: Long,
    @SerializedName("project_id")
    val projectID: Long
)


