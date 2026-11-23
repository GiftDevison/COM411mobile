package com.example.studentstudyplanner.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.studentstudyplanner.data.local.entity.AssignmentEntity
import com.example.studentstudyplanner.data.local.entity.CourseEntity
import com.example.studentstudyplanner.data.local.entity.ExamEntity
import com.example.studentstudyplanner.data.repository.AssignmentRepository
import com.example.studentstudyplanner.data.repository.CourseRepository
import com.example.studentstudyplanner.data.repository.ExamRepository
import com.example.studentstudyplanner.work.WorkScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val courses: List<CourseEntity> = emptyList(),
    val pendingAssignments: List<AssignmentEntity> = emptyList(),
    val upcomingExams: List<ExamEntity> = emptyList(),
    val isLoading: Boolean = false
)

class HomeViewModel(
    application: Application,
    private val courseRepository: CourseRepository,
    private val assignmentRepository: AssignmentRepository,
    private val examRepository: ExamRepository
) : AndroidViewModel(application) {

    val uiState: StateFlow<HomeUiState> = combine(
        courseRepository.allCourses,
        assignmentRepository.allAssignments,
        examRepository.allExams
    ) { courses, assignments, exams ->
        HomeUiState(
            courses = courses,
            pendingAssignments = assignments.filter { !it.isCompleted },
            upcomingExams = exams,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true)
    )

    fun addCourse(name: String, code: String, instructor: String, room: String, credits: Int) {
        viewModelScope.launch {
            courseRepository.insertCourse(
                CourseEntity(
                    courseName = name,
                    courseCode = code,
                    lecturerName = instructor,
                    room = room,
                    credits = credits
                )
            )
        }
    }

    fun addAssignment(courseId: Long, title: String, description: String, dueDateTimestamp: Long, priority: String) {
        viewModelScope.launch {
            val assignment = AssignmentEntity(
                courseId = courseId,
                title = title,
                description = description,
                dueDateTimestamp = dueDateTimestamp,
                priority = priority
            )
            val newId = assignmentRepository.insertAssignment(assignment)
            WorkScheduler.scheduleAssignmentReminder(getApplication(), assignment.copy(id = newId))
        }
    }

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

    class Factory(
        private val application: Application,
        private val courseRepository: CourseRepository,
        private val assignmentRepository: AssignmentRepository,
        private val examRepository: ExamRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(application, courseRepository, assignmentRepository, examRepository) as T
        }
    }
}
