package com.example.studentstudyplanner.ui.screens.assignments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import com.example.studentstudyplanner.utils.DateTimeUtils
import com.example.studentstudyplanner.viewmodel.AssignmentsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentsScreen(
    viewModel: AssignmentsViewModel,
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var assignmentToEdit by remember { mutableStateOf<AssignmentEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Assignments & Deadlines",
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
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Assignment")
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
            if (uiState.assignments.isEmpty()) {
                EmptyStateMessage(
                    title = "No assignments yet.",
                    subtitle = "Tap the + button to add an assignment.",
                    modifier = Modifier.padding(paddingValues)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.assignments, key = { it.id }) { assignment ->
                        val course = uiState.courses.find { it.id == assignment.courseId }
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = assignment.isCompleted,
                                    onCheckedChange = { viewModel.toggleAssignmentCompletion(assignment) }
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = assignment.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textDecoration = if (assignment.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                                    )
                                    if (course != null) {
                                        Text(
                                            text = "Course: ${course.courseName} (${course.courseCode})",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Text(
                                        text = "Due: ${DateTimeUtils.formatDate(assignment.dueDateTimestamp)}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "Priority: ${assignment.priority}",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = when (assignment.priority) {
                                            "HIGH" -> MaterialTheme.colorScheme.error
                                            "MEDIUM" -> MaterialTheme.colorScheme.secondary
                                            else -> MaterialTheme.colorScheme.outline
                                        }
                                    )
                                }
                                Row {
                                    IconButton(onClick = { assignmentToEdit = assignment }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit Assignment",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    IconButton(onClick = { viewModel.deleteAssignment(assignment) }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete Assignment",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showAddDialog) {
            AddAssignmentDialog(
                courses = uiState.courses,
                onDismiss = { showAddDialog = false },
                onConfirm = { courseId, title, description, dueDate, priority ->
                    viewModel.addAssignment(courseId, title, description, dueDate, priority)
                    showAddDialog = false
                }
            )
        }

        if (assignmentToEdit != null) {
            AddAssignmentDialog(
                courses = uiState.courses,
                assignmentToEdit = assignmentToEdit,
                onDismiss = { assignmentToEdit = null },
                onConfirm = { courseId, title, description, dueDate, priority ->
                    val updated = assignmentToEdit!!.copy(
                        courseId = courseId,
                        title = title,
                        description = description,
                        dueDateTimestamp = dueDate,
                        priority = priority
                    )
                    viewModel.updateAssignment(updated)
                    assignmentToEdit = null
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssignmentDialog(
    courses: List<CourseEntity>,
    assignmentToEdit: AssignmentEntity? = null,
    onDismiss: () -> Unit,
    onConfirm: (courseId: Long, title: String, description: String, dueDateTimestamp: Long, priority: String) -> Unit
) {
    var title by remember { mutableStateOf(assignmentToEdit?.title ?: "") }
    var description by remember { mutableStateOf(assignmentToEdit?.description ?: "") }
    var selectedCourse by remember { mutableStateOf(courses.find { it.id == assignmentToEdit?.courseId } ?: courses.firstOrNull()) }
    var priority by remember { mutableStateOf(assignmentToEdit?.priority ?: "MEDIUM") }
    var courseDropdownExpanded by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var courseError by remember { mutableStateOf<String?>(null) }

    val initialDays = if (assignmentToEdit != null && assignmentToEdit.dueDateTimestamp > 0L) {
        ((assignmentToEdit.dueDateTimestamp - System.currentTimeMillis()) / 86400000L).coerceAtLeast(1L).toInt()
    } else {
        3
    }
    var dueDaysText by remember { mutableStateOf(initialDays.toString()) }
    val calculatedDueDate = System.currentTimeMillis() + ((dueDaysText.toLongOrNull() ?: 3L) * 86400000L)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (assignmentToEdit != null) "Edit Assignment" else "Add New Assignment",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (courses.isEmpty() && assignmentToEdit == null) {
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "⚠️ You must add a course first before adding an assignment.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                if (courseError != null) {
                    Text(
                        text = courseError!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { 
                        title = it
                        if (it.isNotBlank()) showError = false
                    },
                    label = { Text("Assignment Title *") },
                    singleLine = true,
                    isError = showError,
                    modifier = Modifier.fillMaxWidth()
                )
                if (showError) {
                    Text(
                        text = "Assignment title is required",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                if (courses.isNotEmpty()) {
                    ExposedDropdownMenuBox(
                        expanded = courseDropdownExpanded,
                        onExpandedChange = { courseDropdownExpanded = !courseDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedCourse?.courseName ?: "Select Course",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Course *") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = courseDropdownExpanded) },
                            modifier = Modifier
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = courseDropdownExpanded,
                            onDismissRequest = { courseDropdownExpanded = false }
                        ) {
                            courses.forEach { course ->
                                DropdownMenuItem(
                                    text = { Text(course.courseName, style = MaterialTheme.typography.bodyLarge) },
                                    onClick = {
                                        selectedCourse = course
                                        courseDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
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
                    if (courses.isEmpty() && assignmentToEdit == null) {
                        courseError = "Please add a course first before adding an assignment."
                        return@TextButton
                    }
                    if (title.isNotBlank()) {
                        val courseId = selectedCourse?.id ?: 0L
                        onConfirm(courseId, title, description, calculatedDueDate, priority)
                    } else {
                        showError = true
                    }
                }
            ) {
                Text(
                    text = if (assignmentToEdit != null) "Save" else "Add",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    )
}
