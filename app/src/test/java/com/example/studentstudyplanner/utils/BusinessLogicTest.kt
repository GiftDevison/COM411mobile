package com.example.studentstudyplanner.utils

import com.example.studentstudyplanner.data.local.entity.AssignmentEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BusinessLogicTest {

    private fun isAssignmentOverdue(dueDateTimestamp: Long, isCompleted: Boolean, currentTime: Long = System.currentTimeMillis()): Boolean {
        return !isCompleted && dueDateTimestamp < currentTime
    }

    private fun getDayName(dayOfWeek: Int): String {
        val dayNames = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        return dayNames.getOrElse(dayOfWeek - 1) { "Invalid Day" }
    }

    @Test
    fun testOverdueDetectionLogic() {
        val pastTime = System.currentTimeMillis() - 86400000L // Yesterday
        val futureTime = System.currentTimeMillis() + 86400000L // Tomorrow

        // Incomplete and past due -> Overdue
        assertTrue(isAssignmentOverdue(pastTime, false))

        // Completed and past due -> Not Overdue
        assertFalse(isAssignmentOverdue(pastTime, true))

        // Incomplete and future due -> Not Overdue
        assertFalse(isAssignmentOverdue(futureTime, false))
    }

    @Test
    fun testDayOfWeekMapping() {
        assertEquals("Monday", getDayName(1))
        assertEquals("Wednesday", getDayName(3))
        assertEquals("Sunday", getDayName(7))
        assertEquals("Invalid Day", getDayName(8))
    }

    @Test
    fun testAssignmentEntityDefaults() {
        val assignment = AssignmentEntity(
            title = "Physics Lab Report",
            courseId = 1L
        )
        assertEquals("Physics Lab Report", assignment.title)
        assertEquals(1L, assignment.courseId)
        assertFalse(assignment.isCompleted)
        assertEquals("MEDIUM", assignment.priority)
        assertEquals("", assignment.description)
    }
}
