package com.task.management.workflow.task.data.remote

import com.google.gson.annotations.SerializedName
import com.task.management.workflow.task.domain.Task
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
    var userId: Long,
    var status: TaskStatus
) {
    fun toTask() = Task(
        taskId,
        name,
        description,
        dueDate,
        userId,
        projectId,
        status
    )
}
