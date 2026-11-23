package com.example.studentstudyplanner.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Courses : Screen("courses", "Courses", Icons.Default.Book)
    object Assignments : Screen("assignments", "Tasks", Icons.AutoMirrored.Filled.Assignment)
    object Exams : Screen("exams", "Exams", Icons.Default.Event)
    object Schedule : Screen("schedule", "Schedule", Icons.Default.Schedule)
    object Reminders : Screen("reminders", "Alerts", Icons.Default.Notifications)

    companion object {
        val bottomNavItems = listOf(
            Home,
            Courses,
            Assignments,
            Exams,
            Schedule,
            Reminders
        )
    }
}
