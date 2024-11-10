package com.task.management.workflow.task.presentation.taskCards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.task.management.workflow.task.domain.Task
import com.task.management.workflow.task.domain.TaskStatus
import com.task.management.workflow.ui.theme.FailureColor
import com.task.management.workflow.ui.theme.HighlightColor
import com.task.management.workflow.ui.theme.RecentColor
import com.task.management.workflow.ui.theme.SuccessColor
import com.task.management.workflow.ui.theme.WarningColor

@Composable
fun TaskCard(
    task: Task,
    onTaskSelected: (Task) -> Unit,
    onDeleteClicked: (Task) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .clickable { onTaskSelected(task) }
    ) {
        Column(modifier = Modifier.padding(top = 10.dp, end = 16.dp)) {
            Text(text = task.dueDate, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Right)
            Box(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(10.dp)
                    .fillMaxWidth(0.4f)
                    .clip(RoundedCornerShape(8.dp))
                    .align(Alignment.End)
                    .background(
                        when (task.status) {
                            TaskStatus.NEW -> RecentColor
                            TaskStatus.PENDING -> WarningColor
                            TaskStatus.COMPLETED -> SuccessColor
                            TaskStatus.OVERDUE -> FailureColor
                            TaskStatus.COMPLETED_OVERDUE -> HighlightColor
                        }
                    )
            )
            Column(modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)){
                Text(text = task.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = task.description)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { onDeleteClicked(task) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Task")
                    }
                }
            }
        }
    }
}