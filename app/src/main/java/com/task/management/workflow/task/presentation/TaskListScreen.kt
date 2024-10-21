package com.task.management.workflow.task.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.task.management.workflow.task.domain.Task
import com.task.management.workflow.task.domain.TaskStatus
import com.task.management.workflow.ui.theme.FailureColor
import com.task.management.workflow.ui.theme.HighlightColor
import com.task.management.workflow.ui.theme.RecentColor
import com.task.management.workflow.ui.theme.SuccessColor
import com.task.management.workflow.ui.theme.WarningColor


@Composable
fun TaskListScreen(viewModel: TaskListViewModel, navController: NavController){
    val state = viewModel.state.value

    val statusItemId = viewModel.statusItemId.value;

    val statusList = listOf(
        StatusItem(1, "ALL"),
        StatusItem(2, "NEW"),
        StatusItem(3, "PENDING"),
        StatusItem(4, "COMPLETED"),
        StatusItem(5, "OVERDUE"),
        StatusItem(6, "COMPLETED_OVERDUE")
    )

    Scaffold { paddingValues ->
        Column (modifier = Modifier.padding(paddingValues)) {

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 10.dp,
                        bottom = 10.dp
                    ),
                value = viewModel.projectId.value.toString(),
                onValueChange = {
                    viewModel.onProjectIdChanged(it)
                },
                label = { Text(text = "Project ID") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            LazyRow (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                items(statusList) { statusItem ->
                    FilterChip(
                        modifier = Modifier.padding(4.dp),
                        selected = statusItemId == statusItem.id,
                        label = {
                         Text(statusItem.description)
                        },
                        leadingIcon = {
                            if(statusItemId == statusItem.id) {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        },
                        onClick = {
                            viewModel.onStatusItemIDChanged(viewModel.projectId.value, statusItem.id,statusItem.description)
                        }
                    )
                }
            }

            if (state.isLoading){
                CircularProgressIndicator()
            }
            if (state.error.isNotEmpty()){
                Text(state.error)
            }
            state.data?.let { taskList: List<Task> ->
                LazyColumn {
                    items(taskList) { task ->
                        Card (modifier = Modifier
                            .padding(
                                start = 20.dp,
                                end = 20.dp,
                                top = 10.dp,
                                bottom = 10.dp
                            )
                            .fillMaxWidth()
                        ) {
                            Column (modifier = Modifier
                                .padding(
                                    top = 10.dp,
                                    end = 16.dp
                                )
                            ) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                    ,
                                    text = task.dueDate,
                                    textAlign = TextAlign.Right,
                                )

                                Box(
                                    modifier = Modifier
                                        .padding(
                                            bottom = 4.dp
                                        )
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
                            }

                            Column(modifier = Modifier
                                .padding(
                                    start = 16.dp,
                                    bottom = 16.dp
                                )
                            ) {
                                Text(
                                    text = task.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .padding(bottom = 6.dp)
                                )
                                Text(
                                    text = task.description
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}