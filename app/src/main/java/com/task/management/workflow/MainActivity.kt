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
import com.task.management.workflow.iam.presentation.sign_in.SignInScreen
import com.task.management.workflow.iam.presentation.sign_in.SignInViewModel
import com.task.management.workflow.iam.presentation.sign_up.SignUpScreen
import com.task.management.workflow.iam.presentation.sign_up.SignUpViewModel
import com.task.management.workflow.profiles.TeammateView
import com.task.management.workflow.project.data.remote.ProjectService
import com.task.management.workflow.task.data.remote.TaskService
import com.task.management.workflow.task.data.repository.TaskRepository
import com.task.management.workflow.task.presentation.taskCreation.TaskCreationScreen
import com.task.management.workflow.task.presentation.taskCreation.TaskCreationViewModel
import com.task.management.workflow.task.presentation.taskList.TaskListScreen
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
        val signInViewModel = SignInViewModel(IAMRepository(iamService), tokenProvider)
        val signUpViewModel = SignUpViewModel(IAMRepository(iamService))

        // Calendar
        val calendarService = retrofit.create(PackageService::class.java)
        val packageRepository = PackageRepository(calendarService)
        val calendarViewModel = PackageListEventsViewModel(packageRepository)

        // Project
        val projectService = retrofit.create(ProjectService::class.java)

        //Task
        val taskService = retrofit.create(TaskService::class.java)
        val taskRepository = TaskRepository(taskService, dao.getTaskDao())
        val taskCreationViewModel = TaskCreationViewModel(taskRepository)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkflowTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "signIn") {
                    //Home is only for testing purposes. It won't be used on the app
                    composable("home") { HomeScreen(navController) }
                    composable("signIn") { SignInScreen(signInViewModel, navController) }
                    composable("signUp") { SignUpScreen(signUpViewModel, navController) }
                    composable("calendar") { PackageListEventScreen(calendarViewModel, navController) }
                    composable("projectCreation") {  }
                    composable("profiles") { TeammateView(navController) }
                    composable("tasks") { TaskCreationScreen(taskCreationViewModel, navController) }
                    composable("taskList") { TaskListScreen(taskCreationViewModel, navController) }
                }
            }
        }
    }
}