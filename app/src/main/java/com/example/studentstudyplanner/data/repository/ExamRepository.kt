package com.example.studentstudyplanner.data.repository

import com.example.studentstudyplanner.data.local.dao.ExamDao
import com.example.studentstudyplanner.data.local.entity.ExamEntity
import kotlinx.coroutines.flow.Flow

class ExamRepository(private val examDao: ExamDao) {
    val allExams: Flow<List<ExamEntity>> = examDao.getAllExams()

    fun getExamsByCourse(courseId: Long): Flow<List<ExamEntity>> {
        return examDao.getExamsByCourse(courseId)
    }

    suspend fun getExamById(id: Long): ExamEntity? {
        return examDao.getExamById(id)
    }

    suspend fun insertExam(exam: ExamEntity): Long {
        return examDao.insertExam(exam)
    }

    suspend fun updateExam(exam: ExamEntity) {
        examDao.updateExam(exam)
    }

    suspend fun deleteExam(exam: ExamEntity) {
        examDao.deleteExam(exam)
    }
}
