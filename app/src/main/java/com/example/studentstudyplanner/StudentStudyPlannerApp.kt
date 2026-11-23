package com.example.studentstudyplanner

import android.app.Application
import com.example.studentstudyplanner.data.local.database.AppDatabase
import com.example.studentstudyplanner.data.repository.AssignmentRepository
import com.example.studentstudyplanner.data.repository.CourseRepository
import com.example.studentstudyplanner.data.repository.ExamRepository
import com.example.studentstudyplanner.data.repository.ReminderRepository
import com.example.studentstudyplanner.data.repository.StudyScheduleRepository
import com.example.studentstudyplanner.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudentStudyPlannerApp : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }

    val courseRepository by lazy { CourseRepository(database.courseDao()) }
    val assignmentRepository by lazy { AssignmentRepository(database.assignmentDao()) }
    val examRepository by lazy { ExamRepository(database.examDao()) }
    val studyScheduleRepository by lazy { StudyScheduleRepository(database.studyScheduleDao()) }
    val reminderRepository by lazy { ReminderRepository(database.reminderDao()) }
    val userRepository by lazy { UserRepository(database.userDao()) }

    override fun onCreate() {
        super.onCreate()
        // Ensure login screen is always displayed before dashboard on app launch
        CoroutineScope(Dispatchers.IO).launch {
            database.userDao().logoutAll()
        }
    }
}
