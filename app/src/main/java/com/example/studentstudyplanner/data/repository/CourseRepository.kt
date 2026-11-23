package com.example.studentstudyplanner.data.repository

import com.example.studentstudyplanner.data.local.dao.CourseDao
import com.example.studentstudyplanner.data.local.entity.CourseEntity
import kotlinx.coroutines.flow.Flow

class CourseRepository(private val courseDao: CourseDao) {
    val allCourses: Flow<List<CourseEntity>> = courseDao.getAllCourses()

    suspend fun getCourseById(id: Long): CourseEntity? {
        return courseDao.getCourseById(id)
    }

    suspend fun insertCourse(course: CourseEntity): Long {
        return courseDao.insertCourse(course)
    }

    suspend fun updateCourse(course: CourseEntity) {
        courseDao.updateCourse(course)
    }

    suspend fun deleteCourse(course: CourseEntity) {
        courseDao.deleteCourse(course)
    }
}
