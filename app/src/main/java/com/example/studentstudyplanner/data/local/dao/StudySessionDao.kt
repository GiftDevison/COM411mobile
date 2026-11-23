package com.example.studentstudyplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.studentstudyplanner.data.local.entity.StudySessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudySessionDao {
    @Query("SELECT * FROM study_sessions ORDER BY date ASC, startTime ASC")
    fun getAllStudySessions(): Flow<List<StudySessionEntity>>

    @Query("SELECT * FROM study_sessions WHERE courseId = :courseId ORDER BY date ASC, startTime ASC")
    fun getStudySessionsByCourse(courseId: Long): Flow<List<StudySessionEntity>>

    @Query("SELECT * FROM study_sessions WHERE id = :id")
    suspend fun getStudySessionById(id: Long): StudySessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudySession(session: StudySessionEntity): Long

    @Update
    suspend fun updateStudySession(session: StudySessionEntity)

    @Delete
    suspend fun deleteStudySession(session: StudySessionEntity)
}
