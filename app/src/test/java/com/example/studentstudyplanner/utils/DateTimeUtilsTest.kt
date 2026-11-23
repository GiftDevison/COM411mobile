package com.example.studentstudyplanner.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class DateTimeUtilsTest {

    @Test
    fun testFormatDate() {
        val calendar = Calendar.getInstance().apply {
            set(2026, Calendar.OCTOBER, 8, 12, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val timestamp = calendar.timeInMillis
        val formatted = DateTimeUtils.formatDate(timestamp, "yyyy-MM-dd")
        assertEquals("2026-10-08", formatted)
    }

    @Test
    fun testFormatDateTime() {
        val calendar = Calendar.getInstance().apply {
            set(2026, Calendar.OCTOBER, 8, 14, 30, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val timestamp = calendar.timeInMillis
        val formatted = DateTimeUtils.formatDateTime(timestamp, "yyyy-MM-dd HH:mm")
        assertEquals("2026-10-08 14:30", formatted)
    }
}
