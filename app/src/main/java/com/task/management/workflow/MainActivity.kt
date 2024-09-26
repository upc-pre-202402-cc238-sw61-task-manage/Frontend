package com.task.management.workflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.task.management.workflow.common.Constants
import com.task.management.workflow.task.data.local.TaskDatabase
import com.task.management.workflow.task.data.remote.TaskService
import com.task.management.workflow.task.data.repository.TaskRepository
import com.task.management.workflow.task.domain.Task
import com.task.management.workflow.task.presentation.taskCreation.TaskCreationViewModel
import com.task.management.workflow.task.presentation.taskCreation.TaskScreen
import com.task.management.workflow.ui.theme.WorkflowTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val service = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TaskService::class.java)

        val dao = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "db-workflow"
        )
            .build()
            .getTaskDao()

        val viewModel = TaskCreationViewModel(TaskRepository(service,dao))

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkflowTheme {
                TaskScreen(viewModel)
            }
        }
    }
}