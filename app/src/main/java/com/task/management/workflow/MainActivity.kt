package com.task.management.workflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.task.management.workflow.calendar.data.remote.PackageService
import com.task.management.workflow.calendar.data.repository.PackageRepository
import com.task.management.workflow.calendar.presentation.PackageListEventScreen
import com.task.management.workflow.calendar.presentation.PackageListEventsViewModel
import com.task.management.workflow.common.Constants
import com.task.management.workflow.iam.data.remote.AuthInterceptor
import com.task.management.workflow.iam.data.remote.IAMService
import com.task.management.workflow.iam.data.remote.TokenProvider
import com.task.management.workflow.iam.data.repository.IAMRepository
import com.task.management.workflow.iam.presentation.sign_in.SignInScreen
import com.task.management.workflow.iam.presentation.sign_in.SignInViewModel
import com.task.management.workflow.iam.presentation.sign_up.SignUpScreen
import com.task.management.workflow.iam.presentation.sign_up.SignUpViewModel
import com.task.management.workflow.ui.theme.WorkflowTheme
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    private val tokenProvider = TokenProvider()
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(tokenProvider))
        .build()

    // IAM
    private val service = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build().create(IAMService::class.java)

    private val signInViewModel = SignInViewModel(IAMRepository(service), tokenProvider)
    private val signUpViewModel = SignUpViewModel(IAMRepository(service))

    // Calendar
    private val calendarService = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build().create(PackageService::class.java)
    private val packageRepository = PackageRepository(calendarService)
    private val calendarViewModel = PackageListEventsViewModel(PackageRepository(calendarService))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkflowTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "signIn") {
                    composable("signIn") { SignInScreen(signInViewModel, navController) }
                    composable("signUp") { SignUpScreen(signUpViewModel, navController) }
                    composable("packageList") { PackageListEventScreen(calendarViewModel, navController) }
                    // Add more composable routes as needed
                }
            }
        }
    }
}