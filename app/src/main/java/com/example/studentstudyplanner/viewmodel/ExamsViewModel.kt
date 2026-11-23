package com.example.studentstudyplanner.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.studentstudyplanner.data.local.entity.CourseEntity
import com.example.studentstudyplanner.data.local.entity.ExamEntity
import com.example.studentstudyplanner.data.repository.CourseRepository
import com.example.studentstudyplanner.data.repository.ExamRepository
import com.example.studentstudyplanner.work.WorkScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ExamsUiState(
    val exams: List<ExamEntity> = emptyList(),
    val courses: List<CourseEntity> = emptyList(),
    val isLoading: Boolean = false
)

class ExamsViewModel(
    application: Application,
    private val examRepository: ExamRepository,
    courseRepository: CourseRepository
) : AndroidViewModel(application) {

    val uiState: StateFlow<ExamsUiState> = combine(
        examRepository.allExams,
        courseRepository.allCourses
    ) { exams, courses ->
        ExamsUiState(
            exams = exams,
            courses = courses,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ExamsUiState(isLoading = true)
    )

    fun addExam(courseId: Long, title: String, examTimestamp: Long, location: String, notes: String) {
        viewModelScope.launch {
            val exam = ExamEntity(
                courseId = courseId,
                title = title,
                examTimestamp = examTimestamp,
                location = location,
                notes = notes
            )
            val newId = examRepository.insertExam(exam)
            WorkScheduler.scheduleExamReminder(getApplication(), exam.copy(id = newId))
        }
    }

    fun updateExam(exam: ExamEntity) {
        viewModelScope.launch {
            examRepository.updateExam(exam)
            WorkScheduler.scheduleExamReminder(getApplication(), exam)
        }
    }

    fun deleteExam(exam: ExamEntity) {
        viewModelScope.launch {
            examRepository.deleteExam(exam)
            WorkScheduler.cancelExamReminder(getApplication(), exam.id)
        }
    }

    class Factory(
        private val application: Application,
        private val examRepository: ExamRepository,
        private val courseRepository: CourseRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ExamsViewModel(application, examRepository, courseRepository) as T
        }
    }
}
