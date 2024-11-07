package com.task.management.workflow.common.main_screen.presentation
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.task.management.workflow.ui.theme.SelectedItemColor
import com.task.management.workflow.ui.theme.SlateBlueColor

@Composable
fun BottomNavigationView(
    navController: NavHostController,
    menuItems: List<BottomNavigationItemMenu>,
    currentRoute: String?
){
    NavigationBar (modifier = Modifier.fillMaxWidth()) {
        menuItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                    )
                },
                label = { Text(item.title) },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = SlateBlueColor,
                    unselectedTextColor = SlateBlueColor
                ),
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (currentRoute == item.route) SelectedItemColor else Color.Transparent
                    )
            )
        }
    }
}