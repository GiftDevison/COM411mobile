package com.example.studentstudyplanner.data.repository

import com.example.studentstudyplanner.data.local.dao.StudyScheduleDao
import com.example.studentstudyplanner.data.local.entity.StudyScheduleEntity
import kotlinx.coroutines.flow.Flow

class StudyScheduleRepository(private val studyScheduleDao: StudyScheduleDao) {
    val allSchedules: Flow<List<StudyScheduleEntity>> = studyScheduleDao.getAllSchedules()

    fun getSchedulesByDay(dayOfWeek: Int): Flow<List<StudyScheduleEntity>> {
        return studyScheduleDao.getSchedulesByDay(dayOfWeek)
    }

    suspend fun getScheduleById(id: Long): StudyScheduleEntity? {
        return studyScheduleDao.getScheduleById(id)
    }

    suspend fun insertSchedule(schedule: StudyScheduleEntity): Long {
        return studyScheduleDao.insertSchedule(schedule)
    }

    suspend fun updateSchedule(schedule: StudyScheduleEntity) {
        studyScheduleDao.updateSchedule(schedule)
    }

    suspend fun deleteSchedule(schedule: StudyScheduleEntity) {
        studyScheduleDao.deleteSchedule(schedule)
    }
}
