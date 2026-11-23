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

data class AssignmentsUiState(
    val assignments: List<AssignmentEntity> = emptyList(),
    val courses: List<CourseEntity> = emptyList(),
    val isLoading: Boolean = false
)

class AssignmentsViewModel(
    application: Application,
    private val assignmentRepository: AssignmentRepository,
    courseRepository: CourseRepository
) : AndroidViewModel(application) {

    val uiState: StateFlow<AssignmentsUiState> = combine(
        assignmentRepository.allAssignments,
        courseRepository.allCourses
    ) { assignments, courses ->
        AssignmentsUiState(
            assignments = assignments,
            courses = courses,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AssignmentsUiState(isLoading = true)
    )

    fun addAssignment(courseId: Long?, title: String, description: String, dueDateTimestamp: Long, priority: String) {
        viewModelScope.launch {
            val assignment = AssignmentEntity(
                courseId = courseId ?: 0L,
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
        private val assignmentRepository: AssignmentRepository,
        private val courseRepository: CourseRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AssignmentsViewModel(application, assignmentRepository, courseRepository) as T
        }
    }
}
