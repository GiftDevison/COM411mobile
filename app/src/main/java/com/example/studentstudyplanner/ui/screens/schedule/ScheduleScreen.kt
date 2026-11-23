package com.example.studentstudyplanner.ui.screens.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.platform.LocalContext
import com.example.studentstudyplanner.data.local.entity.StudyScheduleEntity
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.studentstudyplanner.data.local.entity.CourseEntity
import com.example.studentstudyplanner.ui.components.EmptyStateMessage
import com.example.studentstudyplanner.viewmodel.ScheduleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel,
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var scheduleToEdit by remember { mutableStateOf<StudyScheduleEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Study Schedules",
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
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Schedule")
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
            if (uiState.schedules.isEmpty()) {
                EmptyStateMessage(
                    title = "No study schedules created.",
                    subtitle = "Tap the + button to add a schedule.",
                    modifier = Modifier.padding(paddingValues)
                )
            } else {
                val dayNames = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.schedules, key = { it.id }) { schedule ->
                        val course = uiState.courses.find { it.id == schedule.courseId }
                        val dayName = dayNames.getOrElse(schedule.dayOfWeek - 1) { "Day ${schedule.dayOfWeek}" }
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
                                    checked = schedule.isCompleted,
                                    onCheckedChange = { viewModel.toggleScheduleCompletion(schedule) }
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "$dayName (${schedule.startTime} - ${schedule.endTime})",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textDecoration = if (schedule.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                                    )
                                    if (course != null) {
                                        Text(
                                            text = "Course: ${course.courseName} (${course.courseCode})",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    if (schedule.topic.isNotBlank()) {
                                        Text(
                                            text = "Topic: ${schedule.topic}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    if (schedule.location.isNotBlank()) {
                                        Text(
                                            text = "Location: ${schedule.location}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                Row {
                                    IconButton(onClick = { scheduleToEdit = schedule }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit Schedule",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    IconButton(onClick = { viewModel.deleteSchedule(schedule) }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete Schedule",
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
            AddScheduleDialog(
                courses = uiState.courses,
                onDismiss = { showAddDialog = false },
                onConfirm = { courseId, dayOfWeek, startTime, endTime, topic, location ->
                    viewModel.addSchedule(courseId, dayOfWeek, startTime, endTime, topic, location)
                    showAddDialog = false
                }
            )
        }

        if (scheduleToEdit != null) {
            AddScheduleDialog(
                courses = uiState.courses,
                scheduleToEdit = scheduleToEdit,
                onDismiss = { scheduleToEdit = null },
                onConfirm = { courseId, dayOfWeek, startTime, endTime, topic, location ->
                    val updated = scheduleToEdit!!.copy(
                        courseId = courseId,
                        dayOfWeek = dayOfWeek,
                        startTime = startTime,
                        endTime = endTime,
                        topic = topic,
                        location = location
                    )
                    viewModel.updateSchedule(updated)
                    scheduleToEdit = null
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleDialog(
    courses: List<CourseEntity>,
    scheduleToEdit: StudyScheduleEntity? = null,
    onDismiss: () -> Unit,
    onConfirm: (courseId: Long, dayOfWeek: Int, startTime: String, endTime: String, topic: String, location: String) -> Unit
) {
    val dayNames = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    var selectedDayIndex by remember { mutableIntStateOf(if (scheduleToEdit != null && scheduleToEdit.dayOfWeek in 1..7) scheduleToEdit.dayOfWeek - 1 else 0) }
    var startTime by remember { mutableStateOf(scheduleToEdit?.startTime ?: "09:00") }
    var endTime by remember { mutableStateOf(scheduleToEdit?.endTime ?: "10:30") }
    var topic by remember { mutableStateOf(scheduleToEdit?.topic ?: "") }
    var location by remember { mutableStateOf(scheduleToEdit?.location ?: "") }
    var selectedCourse by remember { mutableStateOf(courses.find { it.id == scheduleToEdit?.courseId } ?: courses.firstOrNull()) }

    var dayDropdownExpanded by remember { mutableStateOf(false) }
    var courseDropdownExpanded by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (scheduleToEdit != null) "Edit Study Schedule" else "Add Study Schedule",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (courses.isEmpty()) {
                    Text(
                        text = "⚠️ You must add at least one course before creating a study schedule.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    ExposedDropdownMenuBox(
                        expanded = dayDropdownExpanded,
                        onExpandedChange = { dayDropdownExpanded = !dayDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = dayNames[selectedDayIndex],
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Day of Week") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dayDropdownExpanded) },
                            modifier = Modifier
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = dayDropdownExpanded,
                            onDismissRequest = { dayDropdownExpanded = false }
                        ) {
                            dayNames.forEachIndexed { index, day ->
                                DropdownMenuItem(
                                    text = { Text(day, style = MaterialTheme.typography.bodyLarge) },
                                    onClick = {
                                        selectedDayIndex = index
                                        dayDropdownExpanded = false
                                    }
                                )
                            }
                        }
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
                            isError = showError,
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
                                        showError = false
                                        courseDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    if (showError) {
                        Text(
                            text = "Please select a course for this schedule",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = startTime,
                            onValueChange = { startTime = it },
                            label = { Text("Start Time") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = endTime,
                            onValueChange = { endTime = it },
                            label = { Text("End Time") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = topic,
                        onValueChange = { topic = it },
                        label = { Text("Topic / Chapter") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location (Library/Home)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            if (courses.isNotEmpty()) {
                TextButton(
                    onClick = {
                        if (selectedCourse != null) {
                            onConfirm(selectedCourse!!.id, selectedDayIndex + 1, startTime, endTime, topic, location)
                        } else {
                            showError = true
                        }
                    }
                ) {
                    Text(
                        text = if (scheduleToEdit != null) "Save" else "Add",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
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
