package com.example.studentstudyplanner.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.studentstudyplanner.data.local.entity.AssignmentEntity
import com.example.studentstudyplanner.data.local.entity.ReminderEntity
import com.example.studentstudyplanner.data.repository.AssignmentRepository
import com.example.studentstudyplanner.data.repository.ReminderRepository
import com.example.studentstudyplanner.utils.NotificationUtils
import com.example.studentstudyplanner.work.WorkScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class RemindersUiState(
    val reminders: List<ReminderEntity> = emptyList(),
    val tasks: List<AssignmentEntity> = emptyList(),
    val isLoading: Boolean = false,
)

class RemindersViewModel(
    application: Application,
    private val reminderRepository: ReminderRepository,
    assignmentRepository: AssignmentRepository
) : AndroidViewModel(application) {

    val uiState: StateFlow<RemindersUiState> = combine(
        reminderRepository.allReminders,
        assignmentRepository.allAssignments
    ) { reminders, tasks ->
        RemindersUiState(
            reminders = reminders,
            tasks = tasks,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RemindersUiState(isLoading = true)
    )

    fun addReminder(title: String, description: String, reminderTimestamp: Long, targetType: String = "GENERAL") {
        val trimmedTitle = title.trim()
        if (trimmedTitle.isBlank()) return

        viewModelScope.launch {
            val reminder = ReminderEntity(
                title = trimmedTitle,
                description = description.trim(),
                reminderTimestamp = reminderTimestamp,
                targetType = targetType,
                isCompleted = false
            )
            val newId = reminderRepository.insertReminder(reminder)
            val inserted = reminder.copy(id = newId)

            WorkScheduler.scheduleReminder(getApplication(), inserted)
            
            // Only show pop-up alert if task is active / not completed
            if (!inserted.isCompleted) {
                NotificationUtils.showReminderNotification(
                    getApplication(),
                    "Study Alert: $trimmedTitle",
                    description.ifBlank { "You have a new study reminder scheduled!" }
                )
            }
        }
    }

    fun updateReminder(reminder: ReminderEntity) {
        viewModelScope.launch {
            reminderRepository.updateReminder(reminder)
            WorkScheduler.scheduleReminder(getApplication(), reminder)
        }
    }

    fun toggleReminderCompletion(reminder: ReminderEntity) {
        viewModelScope.launch {
            val updated = reminder.copy(isCompleted = !reminder.isCompleted)
            reminderRepository.updateReminder(updated)
            WorkScheduler.scheduleReminder(getApplication(), updated)
        }
    }

    fun deleteReminder(reminder: ReminderEntity) {
        viewModelScope.launch {
            reminderRepository.deleteReminder(reminder)
            WorkScheduler.cancelReminder(getApplication(), reminder.id)
        }
    }

    class Factory(
        private val application: Application,
        private val reminderRepository: ReminderRepository,
        private val assignmentRepository: AssignmentRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RemindersViewModel(application, reminderRepository, assignmentRepository) as T
        }
    }
}
