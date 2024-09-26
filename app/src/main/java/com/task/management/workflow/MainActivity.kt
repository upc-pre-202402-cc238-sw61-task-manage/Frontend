package com.task.management.workflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.task.management.workflow.calendar.data.remote.PackageService
import com.task.management.workflow.calendar.data.repository.PackageRepository
import com.task.management.workflow.calendar.presentation.PackageListEventScreen
import com.task.management.workflow.calendar.presentation.PackageListEventsViewModel
import com.task.management.workflow.common.Constants
import com.task.management.workflow.ui.theme.WorkflowTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(
            GsonConverterFactory.create()).build()
        val service = retrofit.create(PackageService::class.java)
        val repository = PackageRepository(service)
        val viewModel = PackageListEventsViewModel(repository)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkflowTheme {
                PackageListEventScreen(viewModel)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WorkflowTheme {
        Greeting("Android")
    }
}