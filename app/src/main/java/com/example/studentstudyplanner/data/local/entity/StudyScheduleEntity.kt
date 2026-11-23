package com.example.studentstudyplanner.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "study_schedules",
    foreignKeys = [
        ForeignKey(
            entity = CourseEntity::class,
            parentColumns = ["id"],
            childColumns = ["courseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("courseId")]
)
data class StudyScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val courseId: Long, // Mandatory - no study schedule may exist without its course
    val dayOfWeek: Int, // 1 = Monday, 7 = Sunday
    val startTime: String, // e.g. "09:00"
    val endTime: String, // e.g. "10:30"
    val topic: String = "",
    val location: String = "",
    val isCompleted: Boolean = false
)
