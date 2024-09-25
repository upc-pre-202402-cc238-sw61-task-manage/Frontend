package com.task.management.workflow.task.data.remote

import com.google.gson.annotations.SerializedName
import com.task.management.workflow.task.domain.Task
import java.util.Date

data class TaskDto(
    @SerializedName("task_id")
    val taskId: Int,
    @SerializedName("task_name")
    var name: String,
    @SerializedName("task_description")
    var description: String,
    @SerializedName("due_date")
    var dueDate: Date,
    @SerializedName("user_id")
    var userID: Int,
    @SerializedName("project_id")
    val projectID: Int
)


