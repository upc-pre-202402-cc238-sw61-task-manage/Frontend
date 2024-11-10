package com.task.management.workflow.task.presentation.taskFilter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TaskFilterSection(
    filterOptions: TaskFilterOptions,
    statusList: List<String>,
    onFilterOptionsChanged: (TaskFilterOptions) -> Unit,
    onSearchClicked: () -> Unit
) {
    Column {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            value = filterOptions.projectId,
            onValueChange = { newProjectId ->
                onFilterOptionsChanged(filterOptions.copy(projectId = newProjectId))
            },
            label = { Text(text = "Project ID") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            value = filterOptions.userId,
            onValueChange = { newUserId ->
                onFilterOptionsChanged(filterOptions.copy(userId = newUserId))
            },
            label = { Text(text = "User ID") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Only show my tasks:", modifier = Modifier.padding(end = 10.dp))
            FilterChip(
                selected = filterOptions.onlyShowUser,
                label = { Text("Show my tasks") },
                onClick = { onFilterOptionsChanged(filterOptions.copy(onlyShowUser = !filterOptions.onlyShowUser)) }
            )
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            items(statusList) { statusItem ->
                FilterChip(
                    selected = filterOptions.status == statusItem,
                    label = { Text(statusItem) },
                    onClick = { onFilterOptionsChanged(filterOptions.copy(status = statusItem)) }
                )
            }
        }
        Button(
            onClick = onSearchClicked,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.5f)
        ) {
            Text(text = "Search", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

