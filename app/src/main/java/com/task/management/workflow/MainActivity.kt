package com.task.management.workflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.task.management.workflow.common.Constants
import com.task.management.workflow.iam.data.remote.IAMService
import com.task.management.workflow.iam.data.repository.IAMRepository
import com.task.management.workflow.iam.presentation.sign_in.SignInScreen
import com.task.management.workflow.iam.presentation.sign_in.SignInViewModel
import com.task.management.workflow.iam.presentation.sign_up.SignUpScreen
import com.task.management.workflow.iam.presentation.sign_up.SignUpViewModel
import com.task.management.workflow.ui.theme.WorkflowTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    private val service = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build().create(IAMService::class.java)
    private val signInViewModel = SignInViewModel(IAMRepository(service))
    private val signUpViewModel = SignUpViewModel(IAMRepository(service))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkflowTheme {
                //SignInScreen(signInViewModel)
                SignUpScreen(signUpViewModel)
            }
        }
    }
}