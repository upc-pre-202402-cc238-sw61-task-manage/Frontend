package com.task.management.workflow.task.data.remote

import com.task.management.workflow.task.domain.Task

fun TaskDto.toTask() = Task(taskId,name, description, dueDate, userId, projectId, status)
fun Task.toTaskDto(): TaskDto { return TaskDto(taskId, name, description, dueDate, projectId, userId, status) }