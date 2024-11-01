package com.task.management.workflow.common.main_screen.presentation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.task.management.workflow.common.ViewModelContainer
import com.task.management.workflow.common.constants.NavigationConstants
import com.task.management.workflow.common.main_screen.data.NavigationHost
import com.task.management.workflow.common.main_screen.data.currentRoute

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModelContainer: ViewModelContainer){
    val navController = rememberNavController()

    val currentRoute = currentRoute(navController)

    val navigationItem = listOf(
        BottomNavigationItemMenu.CalendarView,
        BottomNavigationItemMenu.TaskView,
        BottomNavigationItemMenu.ProfileView
    )

    Scaffold(
        bottomBar = {
            if (currentRoute !in listOf(
                    NavigationConstants.SIGN_IN_PATH,
                    NavigationConstants.SIGN_UP_PATH
                )
            ) {
                BottomNavigationView(navController, navigationItem, currentRoute)
            }
        }
    ) {
        NavigationHost(navController, viewModelContainer)
    }
}