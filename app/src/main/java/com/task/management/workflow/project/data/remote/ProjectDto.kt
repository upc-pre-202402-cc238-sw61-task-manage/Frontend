package com.task.management.workflow.project.data.remote

import com.google.gson.annotations.SerializedName

data class ProjectDto(
    @SerializedName("id")
    val projectId: Long? = 0,
    @SerializedName("projectName")
    val title: String,
    @SerializedName("projectDescription")
    val description: String,
    @SerializedName("projectLeader")
    val leader: String,
)