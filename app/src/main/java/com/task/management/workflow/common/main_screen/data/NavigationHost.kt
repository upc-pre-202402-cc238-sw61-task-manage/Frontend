package com.task.management.workflow.common.main_screen.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.task.management.workflow.calendar.presentation.PackageListEventScreen
import com.task.management.workflow.common.ViewModelContainer
import com.task.management.workflow.common.constants.NavigationConstants
import com.task.management.workflow.iam.presentation.account_selector.AccountSelectorScreen
import com.task.management.workflow.iam.presentation.sign_in.SignInScreen
import com.task.management.workflow.iam.presentation.sign_up.SignUpScreen
import com.task.management.workflow.profiles.presentation.TeammateView
import com.task.management.workflow.project.presentation.projectDetails.ProjectDetailScreen
import com.task.management.workflow.project.presentation.projectList.ProjectListScreen
import com.task.management.workflow.task.presentation.dialogs.TaskCreationScreen
import com.task.management.workflow.task.presentation.taskList.TaskListScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationHost(navController: NavHostController, viewModelContainer: ViewModelContainer) {
    NavHost(navController = navController, startDestination = NavigationConstants.SIGN_ACCOUNT_PICKER_PATH) {
        composable(NavigationConstants.SIGN_IN_PATH) { SignInScreen(viewModelContainer.signInViewModel, navController) }
        composable(NavigationConstants.SIGN_ACCOUNT_PICKER_PATH) { AccountSelectorScreen(viewModelContainer.signInViewModel, navController) }
        composable(NavigationConstants.SIGN_UP_PATH) { SignUpScreen(viewModelContainer.signUpViewModel, navController) }
        composable(NavigationConstants.CALENDAR_PATH) { PackageListEventScreen(viewModelContainer.calendarViewModel, navController) }
        composable(NavigationConstants.TASK_LIST_PATH) { TaskListScreen(viewModelContainer.taskViewModel, navController) }
        composable(NavigationConstants.TASK_CREATION_PATH) { TaskCreationScreen(viewModelContainer.taskViewModel, navController) }
        composable(NavigationConstants.PROJECT_LIST_PATH) { ProjectListScreen(viewModelContainer.projectViewModel, navController) }
        composable(
            route = "${NavigationConstants.PROJECT_DETAILS_PATH}/{projectId}",
            arguments = listOf(navArgument("projectId") { type = NavType.LongType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getLong("projectId") ?: 0L
            ProjectDetailScreen(viewModelContainer.projectViewModel, viewModelContainer.projectUserViewModel, navController, projectId)
        }
        composable(NavigationConstants.PROFILE_PATH) { TeammateView(navController, viewModelContainer.profilesViewModel) }
    }
}