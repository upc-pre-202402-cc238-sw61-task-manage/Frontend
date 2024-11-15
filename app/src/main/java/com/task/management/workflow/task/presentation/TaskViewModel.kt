package com.task.management.workflow.task.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.task.management.workflow.common.UIState
import com.task.management.workflow.task.data.repository.TaskRepository
import com.task.management.workflow.task.domain.Task

class TaskViewModel(
    private val taskRepository: TaskRepository
){
    private val _taskState = mutableStateOf(UIState<List<Task>>())
    val taskState: State<UIState<List<Task>>> get() = _taskState


}