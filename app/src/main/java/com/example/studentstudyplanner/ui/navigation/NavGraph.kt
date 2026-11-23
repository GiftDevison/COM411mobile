package com.example.studentstudyplanner.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.studentstudyplanner.StudentStudyPlannerApp
import com.example.studentstudyplanner.ui.screens.assignments.AssignmentsScreen
import com.example.studentstudyplanner.ui.screens.courses.CoursesScreen
import com.example.studentstudyplanner.ui.screens.exams.ExamsScreen
import com.example.studentstudyplanner.ui.screens.home.HomeScreen
import com.example.studentstudyplanner.ui.screens.login.LoginScreen
import com.example.studentstudyplanner.ui.screens.reminders.RemindersScreen
import com.example.studentstudyplanner.ui.screens.schedule.ScheduleScreen
import com.example.studentstudyplanner.viewmodel.AssignmentsViewModel
import com.example.studentstudyplanner.viewmodel.CoursesViewModel
import com.example.studentstudyplanner.viewmodel.ExamsViewModel
import com.example.studentstudyplanner.viewmodel.HomeViewModel
import com.example.studentstudyplanner.viewmodel.LoginViewModel
import com.example.studentstudyplanner.viewmodel.RemindersViewModel
import com.example.studentstudyplanner.viewmodel.ScheduleViewModel

@Composable
fun MainNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current.applicationContext as StudentStudyPlannerApp
    val loginViewModel: LoginViewModel = viewModel(
        factory = LoginViewModel.Factory(context.userRepository)
    )
    val currentUser by loginViewModel.currentUser.collectAsState()

    if (currentUser == null || !currentUser!!.isLoggedIn) {
        LoginScreen(viewModel = loginViewModel)
    } else {
        val navigateBack: () -> Unit = {
            if (!navController.popBackStack()) {
                navController.navigate(Screen.Home.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                }
            }
        }

        Scaffold(
            bottomBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavigationBar {
                    Screen.bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Home.route) {
                    val homeViewModel: HomeViewModel = viewModel(
                        factory = HomeViewModel.Factory(
                            context,
                            context.courseRepository,
                            context.assignmentRepository,
                            context.examRepository
                        )
                    )
                    HomeScreen(
                        viewModel = homeViewModel,
                        studentName = currentUser?.name ?: "Student",
                        onNavigateToCourses = {
                            navController.navigate(Screen.Courses.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onNavigateToAssignments = {
                            navController.navigate(Screen.Assignments.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onNavigateToReminders = {
                            navController.navigate(Screen.Reminders.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onLogout = {
                            loginViewModel.logout()
                        }
                    )
                }

                composable(Screen.Courses.route) {
                    val coursesViewModel: CoursesViewModel = viewModel(
                        factory = CoursesViewModel.Factory(
                            context,
                            context.courseRepository,
                            context.assignmentRepository
                        )
                    )
                    CoursesScreen(
                        viewModel = coursesViewModel,
                        onNavigateBack = navigateBack
                    )
                }

                composable(Screen.Assignments.route) {
                    val assignmentsViewModel: AssignmentsViewModel = viewModel(
                        factory = AssignmentsViewModel.Factory(
                            context,
                            context.assignmentRepository,
                            context.courseRepository
                        )
                    )
                    AssignmentsScreen(
                        viewModel = assignmentsViewModel,
                        onNavigateBack = navigateBack
                    )
                }

                composable(Screen.Exams.route) {
                    val examsViewModel: ExamsViewModel = viewModel(
                        factory = ExamsViewModel.Factory(
                            context,
                            context.examRepository,
                            context.courseRepository
                        )
                    )
                    ExamsScreen(
                        viewModel = examsViewModel,
                        onNavigateBack = navigateBack
                    )
                }

                composable(Screen.Schedule.route) {
                    val scheduleViewModel: ScheduleViewModel = viewModel(
                        factory = ScheduleViewModel.Factory(
                            context,
                            context.studyScheduleRepository,
                            context.courseRepository
                        )
                    )
                    ScheduleScreen(
                        viewModel = scheduleViewModel,
                        onNavigateBack = navigateBack
                    )
                }

                composable(Screen.Reminders.route) {
                    val remindersViewModel: RemindersViewModel = viewModel(
                        factory = RemindersViewModel.Factory(
                            context,
                            context.reminderRepository,
                            context.assignmentRepository
                        )
                    )
                    RemindersScreen(
                        viewModel = remindersViewModel,
                        onNavigateBack = navigateBack
                    )
                }
            }
        }
    }
}
