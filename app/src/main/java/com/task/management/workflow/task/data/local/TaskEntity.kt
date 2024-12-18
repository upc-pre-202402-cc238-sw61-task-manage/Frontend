package com.task.management.workflow.task.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.task.management.workflow.task.domain.TaskStatus

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    @ColumnInfo("task_id")
    val taskId: Long,
    @ColumnInfo("task_name")
    val name: String,
    @ColumnInfo("task_description")
    val description: String,
    @ColumnInfo("due_date")
    val dueDate: String,
    @ColumnInfo("assigned_user")
    val userId: Long,
    @ColumnInfo("assigned_project")
    val projectId: Long,
    @ColumnInfo("status")
    val status: TaskStatus
)
