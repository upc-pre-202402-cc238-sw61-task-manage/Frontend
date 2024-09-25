package com.task.management.workflow.task.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    @ColumnInfo("task_id")
    val taskId: Int,
    @ColumnInfo("task_name")
    val name: String,
    @ColumnInfo("task_description")
    val description: String,
    @ColumnInfo("due_date")
    val dueDate: Date,
    @ColumnInfo("assigned_user")
    val userID: Int,
    @ColumnInfo("assigned_project")
    val projectID: Int
)
