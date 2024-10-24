package com.task.management.workflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.task.management.workflow.calendar.data.remote.PackageService
import com.task.management.workflow.calendar.data.repository.PackageRepository
import com.task.management.workflow.calendar.presentation.PackageListEventScreen
import com.task.management.workflow.calendar.presentation.PackageListEventsViewModel
import com.task.management.workflow.common.Constants
import com.task.management.workflow.common.Database
import com.task.management.workflow.home.HomeScreen
import com.task.management.workflow.iam.data.remote.AuthInterceptor
import com.task.management.workflow.iam.data.remote.IAMService
import com.task.management.workflow.iam.data.remote.TokenProvider
import com.task.management.workflow.iam.data.repository.IAMRepository
import com.task.management.workflow.iam.presentation.account_selector.AccountSelectorScreen
import com.task.management.workflow.iam.presentation.sign_in.SignInScreen
import com.task.management.workflow.iam.presentation.sign_in.SignInViewModel
import com.task.management.workflow.iam.presentation.sign_up.SignUpScreen
import com.task.management.workflow.iam.presentation.sign_up.SignUpViewModel
import com.task.management.workflow.profiles.TeammateView
import com.task.management.workflow.project.data.remote.ProjectService
import com.task.management.workflow.project.data.repository.ProjectRepository
import com.task.management.workflow.project.presentation.projectCreation.ProjectCreationViewModel
import com.task.management.workflow.project.presentation.projectCreation.ProjectScreen
import com.task.management.workflow.project.presentation.projectList.ProjectListScreen
import com.task.management.workflow.task.data.remote.TaskService
import com.task.management.workflow.task.data.repository.TaskRepository
import com.task.management.workflow.task.presentation.TaskCreationScreen
import com.task.management.workflow.task.presentation.TaskListScreen
import com.task.management.workflow.task.presentation.TaskListViewModel
import com.task.management.workflow.ui.theme.WorkflowTheme
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val tokenProvider = TokenProvider()
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenProvider))
            .build()

        val retrofit = Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val iamService = retrofit.create(IAMService::class.java)

        val dao = Room
            .databaseBuilder(applicationContext,Database::class.java, "workflow-db")
            .build()

        // IAM
        val signInViewModel = SignInViewModel(IAMRepository(iamService, dao.getAccountDao()), tokenProvider)
        val signUpViewModel = SignUpViewModel(IAMRepository(iamService, dao.getAccountDao()))

        // Calendar
        val calendarService = retrofit.create(PackageService::class.java)
        val packageRepository = PackageRepository(calendarService)
        val calendarViewModel = PackageListEventsViewModel(packageRepository)

        // Project
        val projectService = retrofit.create(ProjectService::class.java)
        val projectRepository = ProjectRepository(projectService, dao.getProjectDao())
        val projectViewModel = ProjectCreationViewModel(projectRepository)

        //Task
        val taskService = retrofit.create(TaskService::class.java)
        val taskRepository = TaskRepository(taskService, dao.getTaskDao())
        val taskViewModel = TaskListViewModel(taskRepository)

        //AccountSelector



        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkflowTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "accountSelector") {
                    //Home is only for testing purposes. It won't be used on the app
                    composable("home") { HomeScreen(navController) }
                    composable("accountSelector") { AccountSelectorScreen(signInViewModel, navController) }
                    composable("signIn") { SignInScreen(signInViewModel, navController) }
                    composable("signUp") { SignUpScreen(signUpViewModel, navController) }
                    composable("calendar") { PackageListEventScreen(calendarViewModel, navController) }
                    composable("taskList") { TaskListScreen(taskViewModel,navController) }
                    composable("taskCreation") { TaskCreationScreen(taskViewModel, navController) }
                    composable("projectCreation") { ProjectScreen(projectViewModel, navController) }
                    composable("projectList") { ProjectListScreen(projectViewModel, navController) }
                    composable("profiles") { TeammateView(navController) }
                }
            }
        }
    }
}