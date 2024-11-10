package com.task.management.workflow.project.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.task.management.workflow.project.domain.Project

@Entity(tableName = "projects")
data class ProjectEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("project_id")
    val projectId: Long = 0L,
    @ColumnInfo("project_name")
    val title: String,
    @ColumnInfo("project_description")
    val description: String,
    @ColumnInfo("project_leader")
    val projectLeader: String
)

fun ProjectEntity.toProject(): Project {
    return Project(
        title = title,
        description = description,
        leader = projectLeader,
    )
}
