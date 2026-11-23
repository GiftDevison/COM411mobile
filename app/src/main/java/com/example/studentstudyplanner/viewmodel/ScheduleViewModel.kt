package com.example.studentstudyplanner.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.studentstudyplanner.data.local.entity.CourseEntity
import com.example.studentstudyplanner.data.local.entity.StudyScheduleEntity
import com.example.studentstudyplanner.data.repository.CourseRepository
import com.example.studentstudyplanner.data.repository.StudyScheduleRepository
import com.example.studentstudyplanner.work.WorkScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ScheduleUiState(
    val schedules: List<StudyScheduleEntity> = emptyList(),
    val courses: List<CourseEntity> = emptyList(),
    val isLoading: Boolean = false
)

class ScheduleViewModel(
    application: Application,
    private val studyScheduleRepository: StudyScheduleRepository,
    courseRepository: CourseRepository
) : AndroidViewModel(application) {

    val uiState: StateFlow<ScheduleUiState> = combine(
        studyScheduleRepository.allSchedules,
        courseRepository.allCourses
    ) { schedules, courses ->
        ScheduleUiState(
            schedules = schedules,
            courses = courses,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ScheduleUiState(isLoading = true)
    )

    fun addSchedule(courseId: Long, dayOfWeek: Int, startTime: String, endTime: String, topic: String, location: String) {
        viewModelScope.launch {
            val schedule = StudyScheduleEntity(
                courseId = courseId,
                dayOfWeek = dayOfWeek,
                startTime = startTime,
                endTime = endTime,
                topic = topic,
                location = location
            )
            val newId = studyScheduleRepository.insertSchedule(schedule)
            WorkScheduler.scheduleScheduleReminder(getApplication(), schedule.copy(id = newId))
        }
    }

    fun updateSchedule(schedule: StudyScheduleEntity) {
        viewModelScope.launch {
            studyScheduleRepository.updateSchedule(schedule)
            WorkScheduler.scheduleScheduleReminder(getApplication(), schedule)
        }
    }

    fun toggleScheduleCompletion(schedule: StudyScheduleEntity) {
        viewModelScope.launch {
            val updated = schedule.copy(isCompleted = !schedule.isCompleted)
            studyScheduleRepository.updateSchedule(updated)
            WorkScheduler.scheduleScheduleReminder(getApplication(), updated)
        }
    }

    fun deleteSchedule(schedule: StudyScheduleEntity) {
        viewModelScope.launch {
            studyScheduleRepository.deleteSchedule(schedule)
            WorkScheduler.cancelScheduleReminder(getApplication(), schedule.id)
        }
    }

    class Factory(
        private val application: Application,
        private val studyScheduleRepository: StudyScheduleRepository,
        private val courseRepository: CourseRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ScheduleViewModel(application, studyScheduleRepository, courseRepository) as T
        }
    }
}
