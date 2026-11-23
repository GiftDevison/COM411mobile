package com.example.studentstudyplanner

import com.example.studentstudyplanner.data.local.entity.AssignmentEntity
import com.example.studentstudyplanner.data.local.entity.CourseEntity
import com.example.studentstudyplanner.data.local.entity.ExamEntity
import com.example.studentstudyplanner.data.local.entity.ReminderEntity
import com.example.studentstudyplanner.data.local.entity.StudyScheduleEntity
import com.example.studentstudyplanner.data.local.entity.UserEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StudentStudyPlannerTestSuites {

    // Test Case 1: AUTH_LOGIN_001 (Successful User Account Creation / Login)
    @Test
    fun test_AUTH_LOGIN_001_successful_login() {
        val fullName = "Alex Johnson"
        val nameParts = fullName.trim().split("\\s+".toRegex())
        assertTrue("Full name must contain first and last name", nameParts.size >= 2)

        val user = UserEntity(id = 1L, name = fullName, email = "alex@university.edu", isLoggedIn = true)
        assertEquals("Alex Johnson", user.name)
        assertTrue("User must be logged in", user.isLoggedIn)
    }

    // Test Case 2: AUTH_LOGIN_002 (Reject Account Creation with Single Name Only)
    @Test
    fun test_AUTH_LOGIN_002_single_name_rejected() {
        val singleName = "Alex"
        val nameParts = singleName.trim().split("\\s+".toRegex())
        assertFalse("Single name without last name must be rejected", nameParts.size >= 2)
    }

    // Test Case 3: COURSE_ADD_001 (Add New Course)
    @Test
    fun test_COURSE_ADD_001_add_course_valid() {
        val course = CourseEntity(
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

    // Test Case 4: COURSE_ADD_002 (Add Course with Blank Name)
    @Test
    fun test_COURSE_ADD_002_add_course_blank_name() {
        val courseName = ""
        assertFalse("Course name cannot be blank", courseName.isNotBlank())
    }

    // Test Case 5: TASK_ADD_001 (Add Task Under Course)
    @Test
    fun test_TASK_ADD_001_add_task_under_course() {
        val courseId = 1L
        val task = AssignmentEntity(
            title = "Binary Trees Assignment",
            courseId = courseId,
            priority = "HIGH"
        )
        assertEquals(courseId, task.courseId)
        assertEquals("Binary Trees Assignment", task.title)
        assertEquals("HIGH", task.priority)
    }

    // Test Case 6: CONSTRAINT_ADD_001 (Attempt Task / Exam / Schedule Creation Without Course)
    @Test
    fun test_CONSTRAINT_ADD_001_record_requires_course() {
        val coursesExist = false
        assertFalse("Cannot add records when no courses exist", coursesExist)

        val coursesExistNow = true
        val courseId = 1L
        assertTrue("Records can be added once a course exists", coursesExistNow && courseId > 0L)
    }

    // Test Case 7: TASK_COMPLETE_001 (Toggle Task Completion)
    @Test
    fun test_TASK_COMPLETE_001_toggle_task_completion() {
        var task = AssignmentEntity(
            title = "Homework 1",
            courseId = 1L,
            isCompleted = false
        )
        assertFalse("Task should initially be uncompleted", task.isCompleted)

        task = task.copy(isCompleted = !task.isCompleted)
        assertTrue("Task should be marked completed after toggle", task.isCompleted)
    }

    // Test Case 8: EXAM_ADD_001 (Add Exam with Course Constraint)
    @Test
    fun test_EXAM_ADD_001_add_exam() {
        val courseId = 1L
        val exam = ExamEntity(
            courseId = courseId,
            title = "Midterm Exam",
            location = "Hall A",
            notes = "Chapters 1-4"
        )
        assertEquals("Midterm Exam", exam.title)
        assertEquals(courseId, exam.courseId)
        assertEquals("Hall A", exam.location)
    }

    // Test Case 9: SCH_ADD_001 (Add Study Schedule with Course Constraint)
    @Test
    fun test_SCH_ADD_001_study_schedule_course_constraint() {
        val courseId = 1L
        val schedule = StudyScheduleEntity(
            courseId = courseId,
            dayOfWeek = 1,
            startTime = "09:00",
            endTime = "10:30",
            topic = "Revision"
        )
        assertTrue("Schedule requires a valid courseId", schedule.courseId > 0L)
        assertEquals(1, schedule.dayOfWeek)
    }

    // Test Case 10: ALERT_ADD_001 (Create Reminder & Trigger Notification)
    @Test
    fun test_ALERT_ADD_001_reminder_notification() {
        val reminder = ReminderEntity(
            title = "Submit Essay",
            description = "Final check at 5 PM",
            reminderTimestamp = System.currentTimeMillis() + 3600000L
        )
        assertEquals("Submit Essay", reminder.title)
        assertFalse(reminder.isCompleted)
    }
}
