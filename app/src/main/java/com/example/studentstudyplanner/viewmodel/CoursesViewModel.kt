package com.example.studentstudyplanner.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.studentstudyplanner.data.local.entity.AssignmentEntity
import com.example.studentstudyplanner.data.local.entity.CourseEntity
import com.example.studentstudyplanner.data.repository.AssignmentRepository
import com.example.studentstudyplanner.data.repository.CourseRepository
import com.example.studentstudyplanner.work.WorkScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CoursesUiState(
    val courses: List<CourseEntity> = emptyList(),
    val assignments: List<AssignmentEntity> = emptyList(),
    val isLoading: Boolean = false
)

class CoursesViewModel(
    application: Application,
    private val courseRepository: CourseRepository,
    private val assignmentRepository: AssignmentRepository
) : AndroidViewModel(application) {

    val uiState: StateFlow<CoursesUiState> = combine(
        courseRepository.allCourses,
        assignmentRepository.allAssignments
    ) { courses, assignments ->
        CoursesUiState(
            courses = courses,
            assignments = assignments,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CoursesUiState(isLoading = true)
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

    fun updateCourse(course: CourseEntity) {
        viewModelScope.launch {
            courseRepository.updateCourse(course)
        }
    }

    fun deleteCourse(course: CourseEntity) {
        viewModelScope.launch {
            courseRepository.deleteCourse(course)
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

    fun updateAssignment(assignment: AssignmentEntity) {
        viewModelScope.launch {
            assignmentRepository.updateAssignment(assignment)
            WorkScheduler.scheduleAssignmentReminder(getApplication(), assignment)
        }
    }

    fun toggleAssignmentCompletion(assignment: AssignmentEntity) {
        viewModelScope.launch {
            val updated = assignment.copy(isCompleted = !assignment.isCompleted)
            assignmentRepository.updateAssignment(updated)
            WorkScheduler.scheduleAssignmentReminder(getApplication(), updated)
        }
    }

    fun deleteAssignment(assignment: AssignmentEntity) {
        viewModelScope.launch {
            assignmentRepository.deleteAssignment(assignment)
            WorkScheduler.cancelAssignmentReminder(getApplication(), assignment.id)
        }
    }

    class Factory(
        private val application: Application,
        private val courseRepository: CourseRepository,
        private val assignmentRepository: AssignmentRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CoursesViewModel(application, courseRepository, assignmentRepository) as T
        }
    }
}
