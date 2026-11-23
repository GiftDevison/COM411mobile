package com.example.studentstudyplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.studentstudyplanner.data.local.entity.StudyScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyScheduleDao {
    @Query("SELECT * FROM study_schedules ORDER BY dayOfWeek ASC, startTime ASC")
    fun getAllSchedules(): Flow<List<StudyScheduleEntity>>

    @Query("SELECT * FROM study_schedules WHERE dayOfWeek = :dayOfWeek ORDER BY startTime ASC")
    fun getSchedulesByDay(dayOfWeek: Int): Flow<List<StudyScheduleEntity>>

    @Query("SELECT * FROM study_schedules WHERE id = :id")
    suspend fun getScheduleById(id: Long): StudyScheduleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: StudyScheduleEntity): Long

    @Update
    suspend fun updateSchedule(schedule: StudyScheduleEntity)

    @Delete
    suspend fun deleteSchedule(schedule: StudyScheduleEntity)
}
