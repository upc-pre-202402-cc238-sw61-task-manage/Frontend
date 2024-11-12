package com.task.management.workflow.task.presentation.taskList

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.task.management.workflow.task.domain.Task
import com.task.management.workflow.task.presentation.taskCards.TaskCard

@Composable
fun TaskList(
    tasks: List<Task>,
    onTaskSelected: (Task) -> Unit,
    onDeleteClicked: (Long) -> Unit
) {
    LazyColumn {
        items(tasks) { task ->
            TaskCard(
                task = task,
                onTaskSelected = onTaskSelected,
                onDeleteClicked = onDeleteClicked
            )
        }
    }
}
