package com.task.management.workflow.common

data class UIState<T>(
    val isLoading: Boolean = false,
    var data: T? = null,
    val error: String = ""
)