package com.task.management.workflow.common.main_screen.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Work
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
        Icons.Filled.CalendarToday
    )
    data object ProjectView: BottomNavigationItemMenu(
        "Project",
        NavigationConstants.PROJECT_LIST_PATH,
        Icons.Filled.Work
    )
    data object ProfileView: BottomNavigationItemMenu(
        "Profile",
        NavigationConstants.PROFILE_PATH,
        Icons.Filled.Settings
    )
}