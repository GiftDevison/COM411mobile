package com.example.studentstudyplanner.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FormValidationTest {

    private fun validateCourseName(name: String): Boolean {
        return name.isNotBlank()
    }

    private fun validateAssignmentTitle(title: String): Boolean {
        return title.isNotBlank()
    }

    @Test
    fun testCourseNameValidation() {
        assertTrue(validateCourseName("Mathematics"))
        assertFalse(validateCourseName(""))
        assertFalse(validateCourseName("   "))
    }

    @Test
    fun testAssignmentTitleValidation() {
        assertTrue(validateAssignmentTitle("Calculus Homework"))
        assertFalse(validateAssignmentTitle(""))
        assertFalse(validateAssignmentTitle("   "))
    }
}
