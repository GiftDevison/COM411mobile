package com.example.studentstudyplanner

import com.example.studentstudyplanner.data.local.entity.ExamEntity
import com.example.studentstudyplanner.data.local.entity.StudyScheduleEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class ExamAndScheduleTest {

    @Test
    fun testExamEntityCreation() {
        val exam = ExamEntity(
            id = 1L,
            courseId = 1L,
            title = "Midterm Exam",
            location = "Hall A",
            notes = "Chapters 1-4"
        )
        assertEquals("Midterm Exam", exam.title)
        assertEquals(1L, exam.courseId)
        assertEquals("Hall A", exam.location)
    }

    @Test
    fun testStudyScheduleEntityCreation() {
        val schedule = StudyScheduleEntity(
            id = 1L,
            courseId = 1L,
            dayOfWeek = 1, // Monday
            startTime = "09:00",
            endTime = "10:30",
            topic = "Revision"
        )
        assertEquals(1L, schedule.courseId)
        assertEquals(1, schedule.dayOfWeek)
        assertEquals("09:00", schedule.startTime)
        assertFalse(schedule.isCompleted)
    }
}
