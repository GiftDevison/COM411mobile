package com.example.studentstudyplanner.work

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.studentstudyplanner.data.local.entity.AssignmentEntity
import com.example.studentstudyplanner.data.local.entity.ExamEntity
import com.example.studentstudyplanner.data.local.entity.ReminderEntity
import com.example.studentstudyplanner.data.local.entity.StudyScheduleEntity
import java.util.Calendar
import java.util.concurrent.TimeUnit

object WorkScheduler {

    fun scheduleAssignmentReminder(context: Context, assignment: AssignmentEntity) {
        val workName = "assignment_reminder_${assignment.id}"
        val workManager = WorkManager.getInstance(context)

        if (assignment.isCompleted || assignment.dueDateTimestamp <= 0L) {
            workManager.cancelUniqueWork(workName)
            return
        }

        val delayMs = assignment.dueDateTimestamp - System.currentTimeMillis()
        if (delayMs <= 0L) {
            workManager.cancelUniqueWork(workName)
            return
        }

        val inputData = Data.Builder()
            .putString(ReminderWorker.KEY_WORK_TYPE, ReminderWorker.TYPE_ASSIGNMENT)
            .putLong(ReminderWorker.KEY_ITEM_ID, assignment.id)
            .build()

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()

        workManager.enqueueUniqueWork(workName, ExistingWorkPolicy.REPLACE, request)
    }

    fun cancelAssignmentReminder(context: Context, assignmentId: Long) {
        WorkManager.getInstance(context).cancelUniqueWork("assignment_reminder_$assignmentId")
    }

    fun scheduleExamReminder(context: Context, exam: ExamEntity) {
        val workName = "exam_reminder_${exam.id}"
        val workManager = WorkManager.getInstance(context)

        if (exam.examTimestamp <= 0L) {
            workManager.cancelUniqueWork(workName)
            return
        }

        val delayMs = exam.examTimestamp - System.currentTimeMillis()
        if (delayMs <= 0L) {
            workManager.cancelUniqueWork(workName)
            return
        }

        val inputData = Data.Builder()
            .putString(ReminderWorker.KEY_WORK_TYPE, ReminderWorker.TYPE_EXAM)
            .putLong(ReminderWorker.KEY_ITEM_ID, exam.id)
            .build()

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()

        workManager.enqueueUniqueWork(workName, ExistingWorkPolicy.REPLACE, request)
    }

    fun cancelExamReminder(context: Context, examId: Long) {
        WorkManager.getInstance(context).cancelUniqueWork("exam_reminder_$examId")
    }

    fun scheduleScheduleReminder(context: Context, schedule: StudyScheduleEntity) {
        val workName = "schedule_reminder_${schedule.id}"
        val workManager = WorkManager.getInstance(context)

        if (schedule.isCompleted) {
            workManager.cancelUniqueWork(workName)
            return
        }

        val delayMs = calculateNextScheduleDelay(schedule.dayOfWeek, schedule.startTime)
        if (delayMs <= 0L) {
            workManager.cancelUniqueWork(workName)
            return
        }

        val inputData = Data.Builder()
            .putString(ReminderWorker.KEY_WORK_TYPE, ReminderWorker.TYPE_SCHEDULE)
            .putLong(ReminderWorker.KEY_ITEM_ID, schedule.id)
            .build()

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()

        workManager.enqueueUniqueWork(workName, ExistingWorkPolicy.REPLACE, request)
    }

    fun cancelScheduleReminder(context: Context, scheduleId: Long) {
        WorkManager.getInstance(context).cancelUniqueWork("schedule_reminder_$scheduleId")
    }

    fun scheduleReminder(context: Context, reminder: ReminderEntity) {
        val workName = "reminder_${reminder.id}"
        val workManager = WorkManager.getInstance(context)

        if (reminder.isCompleted || reminder.reminderTimestamp <= 0L) {
            workManager.cancelUniqueWork(workName)
            return
        }

        val delayMs = reminder.reminderTimestamp - System.currentTimeMillis()
        if (delayMs <= 0L) {
            workManager.cancelUniqueWork(workName)
            return
        }

        val inputData = Data.Builder()
            .putString(ReminderWorker.KEY_WORK_TYPE, ReminderWorker.TYPE_REMINDER)
            .putLong(ReminderWorker.KEY_ITEM_ID, reminder.id)
            .build()

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()

        workManager.enqueueUniqueWork(workName, ExistingWorkPolicy.REPLACE, request)
    }

    fun cancelReminder(context: Context, reminderId: Long) {
        WorkManager.getInstance(context).cancelUniqueWork("reminder_$reminderId")
    }

    private fun calculateNextScheduleDelay(dayOfWeek: Int, startTimeStr: String): Long {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance()

        val parts = startTimeStr.trim().split(":", " ")
        var hour = parts.getOrNull(0)?.toIntOrNull() ?: 9
        val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0
        if (startTimeStr.uppercase().contains("PM") && hour < 12) hour += 12
        if (startTimeStr.uppercase().contains("AM") && hour == 12) hour = 0

        target.set(Calendar.HOUR_OF_DAY, hour)
        target.set(Calendar.MINUTE, minute)
        target.set(Calendar.SECOND, 0)
        target.set(Calendar.MILLISECOND, 0)

        var targetDay = dayOfWeek
        if (targetDay < 1 || targetDay > 7) targetDay = Calendar.MONDAY

        while (target.get(Calendar.DAY_OF_WEEK) != targetDay || target.before(now)) {
            target.add(Calendar.DAY_OF_MONTH, 1)
        }

        return target.timeInMillis - now.timeInMillis
    }
}
