package com.task.management.workflow.task.data.remote

import com.task.management.workflow.task.domain.Task

fun TaskDto.toTask() = Task(name, description, dueDate, userID, projectID)