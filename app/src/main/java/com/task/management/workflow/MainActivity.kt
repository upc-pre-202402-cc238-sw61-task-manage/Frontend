package com.task.management.workflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.task.management.workflow.common.Constants
import com.task.management.workflow.iam.data.remote.IAMService
import com.task.management.workflow.iam.data.repository.IAMRepository
import com.task.management.workflow.iam.presentation.IAMScreen
import com.task.management.workflow.iam.presentation.IAMViewModel
import com.task.management.workflow.ui.theme.WorkflowTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkflowTheme {
                val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(
                    GsonConverterFactory.create()).build()
                val service = retrofit.create(IAMService::class.java)
                val repository = IAMRepository(service)
                val viewModel = IAMViewModel(repository)
                IAMScreen(viewModel)
            }
        }
    }
}
