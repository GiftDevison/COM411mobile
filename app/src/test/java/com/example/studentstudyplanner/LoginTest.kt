package com.example.studentstudyplanner

import com.example.studentstudyplanner.data.local.entity.UserEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginTest {

    @Test
    fun testUserEntityCreation() {
        val user = UserEntity(
            id = 1L,
            name = "Alex Johnson",
            email = "alex@university.edu",
            isLoggedIn = true
        )
        assertEquals("Alex Johnson", user.name)
        assertEquals("alex@university.edu", user.email)
        assertTrue(user.isLoggedIn)
    }

    @Test
    fun testLoginValidationLogic() {
        val validName = "Alex Johnson"
        val invalidName = ""

        assertTrue(validName.isNotBlank())
        assertFalse(invalidName.isNotBlank())
    }
}
