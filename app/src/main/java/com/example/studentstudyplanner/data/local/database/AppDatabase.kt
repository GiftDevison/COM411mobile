package com.example.studentstudyplanner.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.studentstudyplanner.data.local.dao.AssignmentDao
import com.example.studentstudyplanner.data.local.dao.CourseDao
import com.example.studentstudyplanner.data.local.dao.ExamDao
import com.example.studentstudyplanner.data.local.dao.ReminderDao
import com.example.studentstudyplanner.data.local.dao.StudyScheduleDao
import com.example.studentstudyplanner.data.local.dao.UserDao
import com.example.studentstudyplanner.data.local.entity.AssignmentEntity
import com.example.studentstudyplanner.data.local.entity.CourseEntity
import com.example.studentstudyplanner.data.local.entity.ExamEntity
import com.example.studentstudyplanner.data.local.entity.ReminderEntity
import com.example.studentstudyplanner.data.local.entity.StudyScheduleEntity
import com.example.studentstudyplanner.data.local.entity.UserEntity

@Database(
    entities = [
        CourseEntity::class,
        AssignmentEntity::class,
        ExamEntity::class,
        StudyScheduleEntity::class,
        ReminderEntity::class,
        UserEntity::class
    ],
    version = 10,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun courseDao(): CourseDao
    abstract fun assignmentDao(): AssignmentDao
    abstract fun examDao(): ExamDao
    abstract fun studyScheduleDao(): StudyScheduleDao
    abstract fun reminderDao(): ReminderDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "student_study_planner_db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
