package com.task.management.workflow.project.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("project_id")
    val projectId: Long = 0L,
    @ColumnInfo("project_name")
    val title: String,
    @ColumnInfo("project_description")
    val description: String,
    @ColumnInfo("project_member")
    val member: String,
    @ColumnInfo("project_leader")
    val leader: String,
    @ColumnInfo("project_creation_date")
    val createdAt: String,
)
