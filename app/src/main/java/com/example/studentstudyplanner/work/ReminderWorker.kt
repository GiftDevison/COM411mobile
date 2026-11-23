package com.example.studentstudyplanner.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.studentstudyplanner.data.local.database.AppDatabase
import com.example.studentstudyplanner.utils.DateTimeUtils
import com.example.studentstudyplanner.utils.NotificationUtils

class ReminderWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    companion object {
        const val KEY_WORK_TYPE = "key_work_type"
        const val KEY_ITEM_ID = "key_item_id"

        const val TYPE_ASSIGNMENT = "ASSIGNMENT"
        const val TYPE_EXAM = "EXAM"
        const val TYPE_SCHEDULE = "SCHEDULE"
        const val TYPE_REMINDER = "REMINDER"
    }

    override suspend fun doWork(): Result {
        val workType = inputData.getString(KEY_WORK_TYPE) ?: return Result.failure()
        val itemId = inputData.getLong(KEY_ITEM_ID, -1L)
        if (itemId == -1L) return Result.failure()

        val db = AppDatabase.getDatabase(applicationContext)

        when (workType) {
            TYPE_ASSIGNMENT -> {
                val assignment = db.assignmentDao().getAssignmentById(itemId)
                if (assignment != null && !assignment.isCompleted) {
                    val course = if (assignment.courseId > 0) db.courseDao().getCourseById(assignment.courseId) else null
                    val courseName = course?.courseName ?: "General"
                    val dueStr = DateTimeUtils.formatDate(assignment.dueDateTimestamp)
                    NotificationUtils.showReminderNotification(
                        applicationContext,
                        "Assignment Due: ${assignment.title}",
                        "Course: $courseName | Due: $dueStr",
                    )
                }
            }

            TYPE_EXAM -> {
                val exam = db.examDao().getExamById(itemId)
                if (exam != null) {
                    val course = if (exam.courseId > 0) db.courseDao().getCourseById(exam.courseId) else null
                    val courseName = course?.courseName ?: "General"
                    val examStr = DateTimeUtils.formatDateTime(exam.examTimestamp)
                    val locationPart = if (exam.location.isNotBlank()) " | Room: ${exam.location}" else ""
                    NotificationUtils.showReminderNotification(
                        applicationContext,
                        "Upcoming Exam: ${exam.title}",
                        "Course: $courseName | Date: $examStr$locationPart",
                    )
                }
            }

            TYPE_SCHEDULE -> {
                val schedule = db.studyScheduleDao().getScheduleById(itemId)
                if (schedule != null && !schedule.isCompleted) {
                    val course = if (schedule.courseId > 0) db.courseDao().getCourseById(schedule.courseId) else null
                    val courseName = course?.courseName ?: "Study Session"
                    val topicStr = schedule.topic.ifBlank { courseName }
                    NotificationUtils.showReminderNotification(
                        applicationContext,
                        "Study Session: $topicStr",
                        "Time to begin your $courseName study session (${schedule.startTime} - ${schedule.endTime})!",
                    )
                }
            }

            TYPE_REMINDER -> {
                val reminder = db.reminderDao().getReminderById(itemId)
                if (reminder != null && !reminder.isCompleted) {
                    NotificationUtils.showReminderNotification(
                        applicationContext,
                        "Study Alert: ${reminder.title}",
                        reminder.description.ifBlank { "You have an active study reminder scheduled!" },
                    )
                }
            }
        }

        return Result.success()
    }
}
