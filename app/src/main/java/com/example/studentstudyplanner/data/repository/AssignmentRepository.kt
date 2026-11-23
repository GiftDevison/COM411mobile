package com.example.studentstudyplanner.data.repository

import com.example.studentstudyplanner.data.local.dao.AssignmentDao
import com.example.studentstudyplanner.data.local.entity.AssignmentEntity
import kotlinx.coroutines.flow.Flow

class AssignmentRepository(private val assignmentDao: AssignmentDao) {
    val allAssignments: Flow<List<AssignmentEntity>> = assignmentDao.getAllAssignments()
    val pendingAssignments: Flow<List<AssignmentEntity>> = assignmentDao.getPendingAssignments()

    fun getAssignmentsByCourse(courseId: Long): Flow<List<AssignmentEntity>> {
        return assignmentDao.getAssignmentsByCourse(courseId)
    }

    suspend fun getAssignmentById(id: Long): AssignmentEntity? {
        return assignmentDao.getAssignmentById(id)
    }

    suspend fun insertAssignment(assignment: AssignmentEntity): Long {
        return assignmentDao.insertAssignment(assignment)
    }

    suspend fun updateAssignment(assignment: AssignmentEntity) {
        assignmentDao.updateAssignment(assignment)
    }

    suspend fun deleteAssignment(assignment: AssignmentEntity) {
        assignmentDao.deleteAssignment(assignment)
    }
}
