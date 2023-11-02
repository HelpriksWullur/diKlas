package com.epic.diklas.ui.fragments.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.epic.diklas.R

@Composable
fun NewBottomApp(navController: NavController) {

    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = currentDestination == "attendance",
            onClick = {
                navController.navigate("attendance") {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                }
            },
            icon = {
                if (currentDestination == "attendance") {
                    Icon(
                        painter = painterResource(id = R.drawable.btn_attendance_active),
                        contentDescription = null
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.btn_attendance),
                        contentDescription = null
                    )
                }
            },
            label = {
                Text(
                    text = "absensi",
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                selectedIconColor = Color.Black,
                unselectedTextColor = MaterialTheme.colorScheme.secondary,
            )
        )
        NavigationBarItem(
            selected = currentDestination == "history",
            onClick = {
                navController.navigate("history") {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                }
            },
            icon = {
                if (currentDestination == "history") {
                    Icon(
                        painter = painterResource(id = R.drawable.btn_history_active),
                        contentDescription = null
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.btn_history),
                        contentDescription = null
                    )
                }
            },
            label = {
                Text(
                    text = "kehadiran"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                selectedIconColor = Color.Black,
                unselectedTextColor = MaterialTheme.colorScheme.secondary,
            )
        )
        NavigationBarItem(
            selected = currentDestination == "task",
            onClick = {
                navController.navigate("task") {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                }
            },
            icon = {
                if (currentDestination == "task") {
                    Icon(
                        painter = painterResource(id = R.drawable.btn_task_active),
                        contentDescription = null,
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.btn_task),
                        contentDescription = null,
                    )
                }
            },
            label = {
                Text(
                    text = "tugas"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                selectedIconColor = Color.Black,
                unselectedTextColor = MaterialTheme.colorScheme.secondary,
            )
        )
    }
}
//
//@Preview(
//    name = "Gelap",
//    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
//)
//@Preview(
//    name = "Terang",
//    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
//)
//@Composable
//fun NewBottomAppPreview() {
//    DiKlasTheme {
//        val navController = rememberNavController()
//        NewBottomApp(navController = navController)
//    }
//}