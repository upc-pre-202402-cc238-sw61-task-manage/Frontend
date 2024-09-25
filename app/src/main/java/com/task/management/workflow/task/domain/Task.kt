package com.task.management.workflow.task.domain

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Task(
    var name: String,
    var description: String,
    var dueDate: Date,
    var userID: Int,
    val projectID: Int
)
