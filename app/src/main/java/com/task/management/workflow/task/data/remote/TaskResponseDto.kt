package com.task.management.workflow.task.data.remote

import com.google.gson.annotations.SerializedName

data class TaskResponseDto(
    val response: String,
    @SerializedName("results")
    val tasks: List<TaskDto>?,
    val error: String?
)
