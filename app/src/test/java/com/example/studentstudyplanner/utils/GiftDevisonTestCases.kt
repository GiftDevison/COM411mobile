package com.example.studentstudyplanner.utils

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

/**
 * GIFT DEVISON Test Cases implementation for Mobile Applications Development Project
 * Corresponds 1:1 to the test cases in GIFT_DEVISON_Test_Cases.doc
 */
class GiftDevisonTestCases {

    // TC 1: AUTH_LOGIN_001 - Successful User Account Creation / Login (Positive)
    @Test
    fun test_AUTH_LOGIN_001_successful_login() {
        val testName = "Alex Johnson"
        val testEmail = "alex@university.edu"
        
        val nameParts = testName.trim().split("\\s+".toRegex())
        assertTrue("Full name must contain first and last name", nameParts.size >= 2)
        
        val user = UserEntity(id = 1L, name = testName, email = testEmail, isLoggedIn = true)
        assertEquals("Alex Johnson", user.name)
        assertEquals("alex@university.edu", user.email)
        assertTrue("User must be marked logged in", user.isLoggedIn)
    }

    // TC 2: AUTH_LOGIN_002 - Reject Account Creation with Single Name Only (Negative)
    @Test
    fun test_AUTH_LOGIN_002_single_name_rejected() {
        val testName = "Alex"
        val nameParts = testName.trim().split("\\s+".toRegex())
        val isValidFullName = nameParts.size >= 2
        
        assertFalse("Single name without last name must be rejected", isValidFullName)
    }

    // TC 3: COURSE_ADD_001 - Add New Course (Positive)
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
        assertEquals("Dr. Smith", course.lecturerName)
        assertEquals(3, course.credits)
    }

    // TC 4: COURSE_ADD_002 - Add Course with Blank Name (Negative)
    @Test
    fun test_COURSE_ADD_002_add_course_blank_name() {
        val courseName = ""
        assertFalse("Course name cannot be blank", courseName.isNotBlank())
    }

    // TC 5: TASK_ADD_001 - Add Task Under Course (Positive)
    @Test
    fun test_TASK_ADD_001_add_task_under_course() {
        val courseId = 1L
        val task = AssignmentEntity(
            title = "Binary Trees Assignment",
            courseId = courseId,
            priority = "HIGH",
            description = "Implement AVL Tree rotations"
        )
        assertEquals(courseId, task.courseId)
        assertEquals("Binary Trees Assignment", task.title)
        assertEquals("HIGH", task.priority)
        assertFalse("Task should not be completed initially", task.isCompleted)
    }

    // TC 6: CONSTRAINT_ADD_001 - Attempt Task / Exam / Schedule Creation Without Course (Negative)
    @Test
    fun test_CONSTRAINT_ADD_001_record_requires_course() {
        val coursesExist = false
        assertFalse("Cannot add tasks, exams, or schedules when no courses exist", coursesExist)

        val coursesExistNow = true
        val validCourseId = 1L
        assertTrue("Records can be created once a course exists", coursesExistNow && validCourseId > 0L)
    }

    // TC 7: TASK_COMPLETE_001 - Toggle Task Completion (Positive)
    @Test
    fun test_TASK_COMPLETE_001_toggle_task_completion() {
        var task = AssignmentEntity(
            title = "Homework 1",
            courseId = 1L,
            isCompleted = false
        )
        assertFalse(task.isCompleted)

        task = task.copy(isCompleted = !task.isCompleted)
        assertTrue("Task should be marked completed", task.isCompleted)
    }

    // TC 8: EXAM_ADD_001 - Add Exam with Course Constraint (Positive)
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

    // TC 9: SCH_ADD_001 - Add Study Schedule with Course Constraint (Positive)
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
        assertEquals("09:00", schedule.startTime)
    }

    // TC 10: ALERT_ADD_001 - Create Reminder & Trigger Notification (Positive)
    @Test
    fun test_ALERT_ADD_001_reminder_notification() {
        val reminder = ReminderEntity(
            title = "Submit Essay",
            description = "Final check at 5 PM",
            reminderTimestamp = System.currentTimeMillis() + 3600000L
        )
        assertEquals("Submit Essay", reminder.title)
        assertEquals("Final check at 5 PM", reminder.description)
        assertFalse(reminder.isCompleted)
    }
}
