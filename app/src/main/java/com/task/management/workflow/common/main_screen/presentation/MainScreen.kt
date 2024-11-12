package com.task.management.workflow.common.main_screen.presentation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        BottomNavigationItemMenu.ProjectView,
        BottomNavigationItemMenu.ProfileView
    )

    Scaffold(
        bottomBar = {
            if (currentRoute !in listOf(
                    NavigationConstants.SIGN_IN_PATH,
                    NavigationConstants.SIGN_UP_PATH,
                    NavigationConstants.SIGN_ACCOUNT_PICKER_PATH,
                )
            ) {
                BottomNavigationView(navController, navigationItem, currentRoute)
            }
        }
    ) {
        Box(
            Modifier.padding(bottom = 88.dp)
        ){
            NavigationHost(navController, viewModelContainer)
        }
    }
}