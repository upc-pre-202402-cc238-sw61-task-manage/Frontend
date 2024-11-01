package com.task.management.workflow

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.task.management.workflow.common.ViewModelContainer
import com.task.management.workflow.common.main_screen.presentation.MainScreen
import com.task.management.workflow.ui.theme.WorkflowTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelContainer = ViewModelContainer(applicationContext)

        enableEdgeToEdge()
        setContent {
            WorkflowTheme {
                MainScreen(viewModelContainer)
            }
        }
    }
}



