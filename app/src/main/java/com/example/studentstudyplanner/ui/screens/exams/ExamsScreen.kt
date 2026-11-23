package com.example.studentstudyplanner.ui.screens.exams

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import com.example.studentstudyplanner.data.local.entity.CourseEntity
import com.example.studentstudyplanner.data.local.entity.ExamEntity
import com.example.studentstudyplanner.ui.components.EmptyStateMessage
import com.example.studentstudyplanner.ui.theme.BrightBlue
import com.example.studentstudyplanner.ui.theme.ErrorRed
import com.example.studentstudyplanner.utils.DateTimeUtils
import com.example.studentstudyplanner.viewmodel.ExamsViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamsScreen(
    viewModel: ExamsViewModel,
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var examToEdit by remember { mutableStateOf<ExamEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Exams & Tests",
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
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Exam")
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
            if (uiState.exams.isEmpty()) {
                EmptyStateMessage(
                    title = "No exams scheduled.",
                    subtitle = "Tap the + button to add an exam.",
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
                    items(uiState.exams, key = { it.id }) { exam ->
                        val course = uiState.courses.find { it.id == exam.courseId }
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = exam.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    if (course != null) {
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Course: ${course.courseName} (${course.courseCode})",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = BrightBlue
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Date: ${DateTimeUtils.formatDateTime(exam.examTimestamp)}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    if (exam.location.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Location: ${exam.location}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(onClick = { examToEdit = exam }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit Exam",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    IconButton(onClick = { viewModel.deleteExam(exam) }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete Exam",
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

        if (showAddDialog) {
            AddExamDialog(
                courses = uiState.courses,
                onDismiss = { showAddDialog = false },
                onConfirm = { courseId, title, examTimestamp, location, notes ->
                    viewModel.addExam(courseId, title, examTimestamp, location, notes)
                    showAddDialog = false
                }
            )
        }

        if (examToEdit != null) {
            AddExamDialog(
                courses = uiState.courses,
                examToEdit = examToEdit,
                onDismiss = { examToEdit = null },
                onConfirm = { courseId, title, examTimestamp, location, notes ->
                    val updated = examToEdit!!.copy(
                        courseId = courseId,
                        title = title,
                        examTimestamp = examTimestamp,
                        location = location,
                        notes = notes
                    )
                    viewModel.updateExam(updated)
                    examToEdit = null
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExamDialog(
    courses: List<CourseEntity>,
    examToEdit: ExamEntity? = null,
    onDismiss: () -> Unit,
    onConfirm: (courseId: Long, title: String, examTimestamp: Long, location: String, notes: String) -> Unit
) {
    var title by remember { mutableStateOf(examToEdit?.title ?: "") }
    var location by remember { mutableStateOf(examToEdit?.location ?: "") }
    var notes by remember { mutableStateOf(examToEdit?.notes ?: "") }
    var selectedCourse by remember { mutableStateOf(courses.find { it.id == examToEdit?.courseId } ?: courses.firstOrNull()) }
    var courseDropdownExpanded by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    var examDaysText by remember { mutableStateOf("7") }
    var selectedHour by remember { mutableStateOf(10) }
    var selectedMinute by remember { mutableStateOf(0) }

    val daysOffset = examDaysText.toLongOrNull() ?: 7L
    val baseTime = System.currentTimeMillis() + (daysOffset * 86400000L)
    val cal = Calendar.getInstance().apply {
        timeInMillis = baseTime
        set(Calendar.HOUR_OF_DAY, selectedHour)
        set(Calendar.MINUTE, selectedMinute)
        set(Calendar.SECOND, 0)
    }
    val calculatedExamTimestamp = cal.timeInMillis

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (examToEdit != null) "Edit Exam" else "Add New Exam",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (courses.isEmpty()) {
                    Text(
                        text = "⚠️ You must add at least one course before scheduling an exam.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { 
                            title = it
                            if (it.isNotBlank()) showError = false
                        },
                        label = { Text("Exam Title * (e.g. Midterm)") },
                        singleLine = true,
                        isError = showError,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (showError) {
                        Text(
                            text = "Exam title is required",
                            style = MaterialTheme.typography.bodySmall,
                            color = ErrorRed
                        )
                    }

                    ExposedDropdownMenuBox(
                        expanded = courseDropdownExpanded,
                        onExpandedChange = { courseDropdownExpanded = !courseDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedCourse?.courseName ?: "Select Course *",
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

                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Room / Hall") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notes / Topics Covered") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Exam Date & Time Selection
                    Text(
                        text = "Exam Date & Time: ${DateTimeUtils.formatDateTime(calculatedExamTimestamp)}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = BrightBlue
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(3, 7, 14, 30).forEach { days ->
                            OutlinedButton(
                                onClick = { examDaysText = days.toString() },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(text = "+${days}d", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            Pair("9:00 AM", Pair(9, 0)),
                            Pair("11:00 AM", Pair(11, 0)),
                            Pair("2:00 PM", Pair(14, 0)),
                            Pair("4:00 PM", Pair(16, 0))
                        ).forEach { (label, timePair) ->
                            OutlinedButton(
                                onClick = {
                                    selectedHour = timePair.first
                                    selectedMinute = timePair.second
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(text = label, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (courses.isNotEmpty()) {
                TextButton(
                    onClick = {
                        if (title.isNotBlank() && selectedCourse != null) {
                            onConfirm(selectedCourse!!.id, title, calculatedExamTimestamp, location, notes)
                        } else {
                            showError = true
                        }
                    }
                ) {
                    Text(
                        text = if (examToEdit != null) "Save" else "Add",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = BrightBlue
                    )
                }
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
