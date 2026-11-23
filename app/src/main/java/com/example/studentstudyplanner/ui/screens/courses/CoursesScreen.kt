package com.example.studentstudyplanner.ui.screens.courses

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.studentstudyplanner.data.local.entity.AssignmentEntity
import com.example.studentstudyplanner.data.local.entity.CourseEntity
import com.example.studentstudyplanner.ui.components.EmptyStateMessage
import com.example.studentstudyplanner.ui.theme.BrightBlue
import com.example.studentstudyplanner.ui.theme.ErrorRed
import com.example.studentstudyplanner.ui.theme.SuccessGreen
import com.example.studentstudyplanner.ui.theme.WarningOrange
import com.example.studentstudyplanner.utils.DateTimeUtils
import com.example.studentstudyplanner.viewmodel.CoursesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen(
    viewModel: CoursesViewModel,
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddCourseDialog by remember { mutableStateOf(false) }
    var courseToEdit by remember { mutableStateOf<CourseEntity?>(null) }
    
    // State for adding/editing tasks per course
    var addTaskCourseId by remember { mutableStateOf<Long?>(null) }
    var addTaskCourseName by remember { mutableStateOf("") }
    var taskToEdit by remember { mutableStateOf<AssignmentEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Courses & Tasks",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddCourseDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Course")
            }
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            if (uiState.courses.isEmpty()) {
                EmptyStateMessage(
                    title = "No courses added yet.",
                    subtitle = "Tap the + button to add a course.",
                    modifier = Modifier.padding(paddingValues)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.courses, key = { it.id }) { course ->
                        val courseAssignments = uiState.assignments.filter { it.courseId == course.id }
                        val todoTasks = courseAssignments.filter { !it.isCompleted }
                        val completedTasks = courseAssignments.filter { it.isCompleted }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                // Course Header & Edit/Delete
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = course.courseName,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Code: ${course.courseCode}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        if (course.lecturerName.isNotBlank()) {
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "Instructor: ${course.lecturerName}",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        if (course.room.isNotBlank()) {
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "Room: ${course.room}",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(onClick = { courseToEdit = course }) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Edit Course",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                        IconButton(onClick = { viewModel.deleteCourse(course) }) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete Course",
                                                tint = ErrorRed
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Add Task Button for this Course (Navy Blue)
                                OutlinedButton(
                                    onClick = {
                                        addTaskCourseId = course.id
                                        addTaskCourseName = course.courseName
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.padding(4.dp))
                                    Text("Add Task to ${course.courseName}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                }

                                // To-Do List Section
                                if (todoTasks.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "To-Do List (${todoTasks.size})",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    todoTasks.forEach { task ->
                                        val currentTime = System.currentTimeMillis()
                                        val isOverdue = task.dueDateTimestamp < currentTime
                                        val isDueSoon = !isOverdue && (task.dueDateTimestamp - currentTime <= 48 * 3600 * 1000L)

                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                                            )
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(10.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Checkbox(
                                                    checked = task.isCompleted,
                                                    onCheckedChange = { viewModel.toggleAssignmentCompletion(task) }
                                                )
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = task.title,
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                    if (task.dueDateTimestamp > 0L) {
                                                        Text(
                                                            text = "Due: ${DateTimeUtils.formatDate(task.dueDateTimestamp)}",
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )
                                                    }
                                                    if (isOverdue) {
                                                        Text(
                                                            text = "OVERDUE",
                                                            style = MaterialTheme.typography.labelSmall,
                                                            fontWeight = FontWeight.Bold,
                                                            color = ErrorRed
                                                        )
                                                    } else if (isDueSoon) {
                                                        Text(
                                                            text = "DUE SOON",
                                                            style = MaterialTheme.typography.labelSmall,
                                                            fontWeight = FontWeight.Bold,
                                                            color = WarningOrange
                                                        )
                                                    }
                                                }
                                                IconButton(onClick = { taskToEdit = task }) {
                                                    Icon(
                                                        imageVector = Icons.Default.Edit,
                                                        contentDescription = "Edit Task",
                                                        tint = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                                IconButton(onClick = { viewModel.deleteAssignment(task) }) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = "Delete Task",
                                                        tint = ErrorRed
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                // Completed Tasks Section
                                if (completedTasks.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "Completed Tasks (${completedTasks.size})",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = SuccessGreen
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    completedTasks.forEach { task ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                                            )
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(10.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Checkbox(
                                                    checked = task.isCompleted,
                                                    onCheckedChange = { viewModel.toggleAssignmentCompletion(task) }
                                                )
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = task.title,
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        fontWeight = FontWeight.Medium,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                        textDecoration = TextDecoration.LineThrough
                                                    )
                                                }
                                                IconButton(onClick = { viewModel.deleteAssignment(task) }) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = "Delete Task",
                                                        tint = ErrorRed
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Add Course Dialog
        if (showAddCourseDialog) {
            AddCourseDialog(
                onDismiss = { showAddCourseDialog = false },
                onConfirm = { name, code, instructor, room, credits ->
                    viewModel.addCourse(name, code, instructor, room, credits)
                    showAddCourseDialog = false
                }
            )
        }

        // Edit Course Dialog
        if (courseToEdit != null) {
            AddCourseDialog(
                courseToEdit = courseToEdit,
                onDismiss = { courseToEdit = null },
                onConfirm = { name, code, instructor, room, credits ->
                    val updated = courseToEdit!!.copy(
                        courseName = name,
                        courseCode = code,
                        lecturerName = instructor,
                        room = room,
                        credits = credits
                    )
                    viewModel.updateCourse(updated)
                    courseToEdit = null
                }
            )
        }

        // Add Task to Course Dialog
        if (addTaskCourseId != null) {
            AddTaskToCourseDialog(
                courseId = addTaskCourseId!!,
                courseName = addTaskCourseName,
                onDismiss = { addTaskCourseId = null },
                onConfirm = { title, description, dueDate, priority ->
                    viewModel.addAssignment(addTaskCourseId!!, title, description, dueDate, priority)
                    addTaskCourseId = null
                }
            )
        }

        // Edit Task Dialog
        if (taskToEdit != null) {
            val course = uiState.courses.find { it.id == taskToEdit!!.courseId }
            AddTaskToCourseDialog(
                courseId = taskToEdit!!.courseId,
                courseName = course?.courseName ?: "Course",
                taskToEdit = taskToEdit,
                onDismiss = { taskToEdit = null },
                onConfirm = { title, description, dueDate, priority ->
                    val updated = taskToEdit!!.copy(
                        title = title,
                        description = description,
                        dueDateTimestamp = dueDate,
                        priority = priority
                    )
                    viewModel.updateAssignment(updated)
                    taskToEdit = null
                }
            )
        }
    }
}

@Composable
fun AddTaskToCourseDialog(
    courseId: Long,
    courseName: String,
    taskToEdit: AssignmentEntity? = null,
    onDismiss: () -> Unit,
    onConfirm: (title: String, description: String, dueDateTimestamp: Long, priority: String) -> Unit
) {
    var title by remember { mutableStateOf(taskToEdit?.title ?: "") }
    var description by remember { mutableStateOf(taskToEdit?.description ?: "") }
    var priority by remember { mutableStateOf(taskToEdit?.priority ?: "MEDIUM") }
    var showError by remember { mutableStateOf(false) }

    val initialDays = if (taskToEdit != null && taskToEdit.dueDateTimestamp > 0L) {
        ((taskToEdit.dueDateTimestamp - System.currentTimeMillis()) / 86400000L).coerceAtLeast(1L).toInt()
    } else {
        3
    }
    var dueDaysText by remember { mutableStateOf(initialDays.toString()) }
    val calculatedDueDate = System.currentTimeMillis() + ((dueDaysText.toLongOrNull() ?: 3L) * 86400000L)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (taskToEdit != null) "Edit Task" else "Add Task to $courseName",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { 
                        title = it
                        if (it.isNotBlank()) showError = false
                    },
                    label = { Text("Task Title *") },
                    singleLine = true,
                    isError = showError,
                    modifier = Modifier.fillMaxWidth()
                )
                if (showError) {
                    Text(
                        text = "Task title is required",
                        style = MaterialTheme.typography.bodySmall,
                        color = ErrorRed
                    )
                }
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Due Date Selection
                Text(
                    text = "Due Date: ${DateTimeUtils.formatDate(calculatedDueDate)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(1, 3, 7, 14, 30).forEach { days ->
                        OutlinedButton(
                            onClick = { dueDaysText = days.toString() },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "+${days}d", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title, description, calculatedDueDate, priority)
                    } else {
                        showError = true
                    }
                }
            ) {
                Text(
                    text = if (taskToEdit != null) "Save" else "Add",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}

@Composable
fun AddCourseDialog(
    courseToEdit: CourseEntity? = null,
    onDismiss: () -> Unit,
    onConfirm: (name: String, code: String, instructor: String, room: String, credits: Int) -> Unit
) {
    var name by remember { mutableStateOf(courseToEdit?.courseName ?: "") }
    var code by remember { mutableStateOf(courseToEdit?.courseCode ?: "") }
    var instructor by remember { mutableStateOf(courseToEdit?.lecturerName ?: "") }
    var room by remember { mutableStateOf(courseToEdit?.room ?: "") }
    var creditsText by remember { mutableStateOf(courseToEdit?.credits?.toString() ?: "3") }
    var showError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (courseToEdit != null) "Edit Course" else "Add New Course",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { 
                        name = it
                        if (it.isNotBlank()) showError = false
                    },
                    label = { Text("Course Name *") },
                    singleLine = true,
                    isError = showError,
                    modifier = Modifier.fillMaxWidth()
                )
                if (showError) {
                    Text(
                        text = "Course name is required",
                        style = MaterialTheme.typography.bodySmall,
                        color = ErrorRed
                    )
                }
                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("Course Code (e.g. CS101)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = instructor,
                    onValueChange = { instructor = it },
                    label = { Text("Instructor") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = room,
                    onValueChange = { room = it },
                    label = { Text("Room / Location") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = creditsText,
                    onValueChange = { creditsText = it },
                    label = { Text("Credits") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        val finalCode = code.ifBlank { name.take(3).uppercase() }
                        val credits = creditsText.toIntOrNull() ?: 3
                        onConfirm(name, finalCode, instructor, room, credits)
                    } else {
                        showError = true
                    }
                }
            ) {
                Text(
                    text = if (courseToEdit != null) "Save" else "Add",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}
