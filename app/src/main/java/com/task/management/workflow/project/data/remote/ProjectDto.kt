package com.task.management.workflow.project.data.remote

import com.google.gson.annotations.SerializedName

data class ProjectDto(
    @SerializedName("id")
    val projectId: Long,
    @SerializedName("projectName")
    val title: String,
    @SerializedName("projectDescription")
    val description: String,
    @SerializedName("projectMember")
    val member: String,
    @SerializedName("projectManager")
    val leader: String,
    @SerializedName("startDate")
    val createdAt: String
)