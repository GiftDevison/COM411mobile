package com.example.studentstudyplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.studentstudyplanner.data.local.entity.AssignmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AssignmentDao {
    @Query("SELECT * FROM assignments ORDER BY dueDateTimestamp ASC")
    fun getAllAssignments(): Flow<List<AssignmentEntity>>

    @Query("SELECT * FROM assignments WHERE isCompleted = 0 ORDER BY dueDateTimestamp ASC")
    fun getPendingAssignments(): Flow<List<AssignmentEntity>>

    @Query("SELECT * FROM assignments WHERE courseId = :courseId ORDER BY dueDateTimestamp ASC")
    fun getAssignmentsByCourse(courseId: Long): Flow<List<AssignmentEntity>>

    @Query("SELECT * FROM assignments WHERE id = :id")
    suspend fun getAssignmentById(id: Long): AssignmentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignment: AssignmentEntity): Long

    @Update
    suspend fun updateAssignment(assignment: AssignmentEntity)

    @Delete
    suspend fun deleteAssignment(assignment: AssignmentEntity)
}
