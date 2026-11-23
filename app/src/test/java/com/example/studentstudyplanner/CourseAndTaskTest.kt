package com.example.studentstudyplanner

import com.example.studentstudyplanner.data.local.entity.AssignmentEntity
import com.example.studentstudyplanner.data.local.entity.CourseEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CourseAndTaskTest {

    @Test
    fun testCourseEntityCreation() {
        val course = CourseEntity(
            id = 1L,
            courseName = "Data Structures",
            courseCode = "CS201",
            lecturerName = "Dr. Smith",
            room = "Lab 3B",
            credits = 3
        )
        assertEquals("Data Structures", course.courseName)
        assertEquals("CS201", course.courseCode)
        assertEquals(3, course.credits)
    }

    @Test
    fun testTaskMandatoryCourseBinding() {
        val task = AssignmentEntity(
            id = 1L,
            title = "Binary Trees Assignment",
            courseId = 1L,
            priority = "HIGH"
        )
        assertEquals(1L, task.courseId)
        assertEquals("Binary Trees Assignment", task.title)
        assertEquals("HIGH", task.priority)
        assertFalse(task.isCompleted)
    }

    @Test
    fun testTaskCompletionToggle() {
        var task = AssignmentEntity(
            id = 1L,
            title = "Homework 1",
            courseId = 1L,
            isCompleted = false
        )
        assertFalse(task.isCompleted)

        // Toggle completion
        task = task.copy(isCompleted = !task.isCompleted)
        assertTrue(task.isCompleted)
    }
}
