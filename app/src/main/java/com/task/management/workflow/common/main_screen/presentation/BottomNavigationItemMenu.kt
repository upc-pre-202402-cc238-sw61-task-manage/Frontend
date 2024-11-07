package com.task.management.workflow.common.main_screen.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Task
import androidx.compose.ui.graphics.vector.ImageVector
import com.task.management.workflow.common.constants.NavigationConstants

sealed class BottomNavigationItemMenu(
    val title: String,
    val route: String,
    val icon: ImageVector
) {
    data object CalendarView: BottomNavigationItemMenu(
        "Calendar",
        NavigationConstants.CALENDAR_PATH,
        Icons.Filled.CalendarToday)
    data object TaskView: BottomNavigationItemMenu(
        "Task",
        NavigationConstants.TASK_LIST_PATH,
        Icons.Filled.Task
    )
    data object ProfileView: BottomNavigationItemMenu(
        "Profile",
        NavigationConstants.PROFILE_PATH,
        Icons.Filled.Settings
    )
}