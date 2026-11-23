package com.example.studentstudyplanner.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "assignments",
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
data class AssignmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val courseId: Long, // Mandatory - no task may exist without its course
    val description: String = "",
    val dueDateTimestamp: Long = 0L,
    val reminderMinutes: Int = 60,
    val isCompleted: Boolean = false,
    val priority: String = "MEDIUM"
)
