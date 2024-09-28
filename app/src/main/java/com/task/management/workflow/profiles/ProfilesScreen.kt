package com.task.management.workflow.profiles

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@Composable
fun TeammateView(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        TopBar()
        Spacer(modifier = Modifier.height(16.dp))
        TeammateCard(
            imageUrl = "https://www.pngall.com/wp-content/uploads/5/Profile.png", // URL temporal para la imagen
            name = "Roberto Ruis",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do architecto beatae...",
            company = "Company Inc.",
            age = 25,
            tasks = listOf(
                "Task 1 - 10/10/202X",
                "Task 2 - 12/12/202X"
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = {
                navController.navigate("packageList")
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Return to calendar", fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = { Text("Teammates", color = Color.White) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF303F9F)),
        actions = {
            IconButton(onClick = { /* TODO: Search action */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu_search),
                    contentDescription = "Search",
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
fun TeammateCard(
    imageUrl: String,
    name: String,
    company: String,
    age: Int,
    description: String,
    tasks: List<String>
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Imagen del compañero de equipo
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Nombre y descripción
            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(text = "Age: $age", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = description, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Company: $company", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(16.dp))
            // Lista de tareas
            Text(text = "Tasks list", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            tasks.forEach { task ->
                TaskItem(task)
            }
        }
    }
}

@Composable
fun TaskItem(task: String) {
    Text(
        text = task,
        fontSize = 14.sp,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTeammateView() {
}