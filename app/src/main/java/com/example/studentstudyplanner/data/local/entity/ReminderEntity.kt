package com.example.studentstudyplanner.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val reminderTimestamp: Long,
    val isCompleted: Boolean = false,
    val targetType: String = "GENERAL", // GENERAL, ASSIGNMENT, EXAM, SCHEDULE
    val targetId: Long? = null
)
