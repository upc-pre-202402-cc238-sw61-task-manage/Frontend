package com.task.management.workflow.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController){
    Scaffold { paddingValues: PaddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF302DA0))
        ){}
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Button(
                modifier = Modifier.padding(4.dp),
                onClick = {
                    navController.navigate("calendar")
                }
            ) {
                Text("Calendar")
            }
            Button(
                modifier = Modifier.padding(4.dp),
                onClick = {
                    navController.navigate("projectCreation")
                }
            ) {
                Text("Project")
            }
            Button(
                modifier = Modifier.padding(4.dp),
                onClick = {
                    navController.navigate("profiles")
                }
            ) {
                Text("Profiles")
            }
        }
    }
}