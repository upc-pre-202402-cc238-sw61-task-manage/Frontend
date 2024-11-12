package com.task.management.workflow.task.presentation.taskFilter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.task.management.workflow.ui.theme.SlateBlueColor

@Composable
fun TaskFilterSection(
    filterOptions: TaskFilterOptions,
    statusList: List<String>,
    onFilterOptionsChanged: (TaskFilterOptions) -> Unit,
    onSearchClicked: () -> Unit
) {
    Column {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            items(statusList) { statusItem ->
                FilterChip(
                    modifier = Modifier.padding(4.dp),
                    selected = filterOptions.status == statusItem,
                    label = { Text(statusItem) },
                    onClick = { onFilterOptionsChanged(filterOptions.copy(status = statusItem)) },
                    leadingIcon = {
                        if(filterOptions.status == statusItem) {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    }
                )
            }
        }
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                selected = filterOptions.onlyShowUser,
                label = { Text("Show only my tasks") },
                onClick = { onFilterOptionsChanged(filterOptions.copy(onlyShowUser = !filterOptions.onlyShowUser)) },
                leadingIcon = {
                    if (filterOptions.onlyShowUser) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Done icon",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = onSearchClicked,
                colors = ButtonColors(
                    SlateBlueColor,
                    Color.White,
                    Color.Gray,
                    Color.Gray
                )
            ) {
                Text(text = "Search", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

