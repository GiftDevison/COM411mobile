package com.example.studentstudyplanner.ui.screens.reminders

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
import com.example.studentstudyplanner.data.local.entity.AssignmentEntity
import com.example.studentstudyplanner.data.local.entity.ReminderEntity
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
import com.example.studentstudyplanner.ui.components.EmptyStateMessage
import com.example.studentstudyplanner.utils.DateTimeUtils
import com.example.studentstudyplanner.viewmodel.RemindersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindersScreen(
    viewModel: RemindersViewModel,
    onNavigateBack: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showAddDialog by remember { mutableStateOf(false) }
    var reminderToEdit by remember { mutableStateOf<ReminderEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Reminders & Notifications",
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
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Reminder")
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
            if (uiState.reminders.isEmpty()) {
                EmptyStateMessage(
                    title = "No active reminders.",
                    subtitle = "Tap the + button to add a reminder.",
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
                    items(uiState.reminders, key = { it.id }) { reminder ->
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
                                    checked = reminder.isCompleted,
                                    onCheckedChange = { viewModel.toggleReminderCompletion(reminder) }
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = reminder.title,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            textDecoration = if (reminder.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                                        )
                                        Surface(
                                            color = if (reminder.isCompleted) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primaryContainer,
                                            shape = MaterialTheme.shapes.extraSmall
                                        ) {
                                            Text(
                                                text = if (reminder.isCompleted) "Task Done" else "Active Alert",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = if (reminder.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimaryContainer,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    if (reminder.description.isNotBlank()) {
                                        Text(
                                            text = reminder.description,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Text(
                                        text = "Alert at: ${DateTimeUtils.formatDateTime(reminder.reminderTimestamp)}",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Row {
                                    IconButton(onClick = { reminderToEdit = reminder }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit Reminder",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    IconButton(onClick = { viewModel.deleteReminder(reminder) }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete Reminder",
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
            AddReminderDialog(
                tasks = uiState.tasks,
                onDismiss = { showAddDialog = false },
                onConfirm = { title, description, reminderTimestamp ->
                    viewModel.addReminder(title, description, reminderTimestamp)
                    showAddDialog = false
                }
            )
        }

        if (reminderToEdit != null) {
            AddReminderDialog(
                tasks = uiState.tasks,
                reminderToEdit = reminderToEdit,
                onDismiss = { reminderToEdit = null },
                onConfirm = { title, description, reminderTimestamp ->
                    val updated = reminderToEdit!!.copy(
                        title = title,
                        description = description,
                        reminderTimestamp = reminderTimestamp
                    )
                    viewModel.updateReminder(updated)
                    reminderToEdit = null
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderDialog(
    tasks: List<AssignmentEntity> = emptyList(),
    reminderToEdit: ReminderEntity? = null,
    onDismiss: () -> Unit,
    onConfirm: (title: String, description: String, reminderTimestamp: Long) -> Unit,
) {
    val activeTasks = tasks.filter { !it.isCompleted }
    var selectedTask by remember { mutableStateOf(tasks.find { it.title == reminderToEdit?.title } ?: activeTasks.firstOrNull()) }
    var title by remember { mutableStateOf(reminderToEdit?.title ?: selectedTask?.title ?: "") }
    var description by remember { mutableStateOf(reminderToEdit?.description ?: selectedTask?.description ?: "") }
    var taskDropdownExpanded by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var taskError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (reminderToEdit != null) "Edit Reminder" else "Add New Reminder",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if ((tasks.isEmpty()) && (reminderToEdit == null)) {
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "⚠️ No tasks found. Please create a task first before adding a reminder.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                } else if (activeTasks.isEmpty() && reminderToEdit == null) {
                    Surface(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "🎉 All tasks are already completed! No pending tasks need reminders.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                if (taskError != null) {
                    Text(
                        text = taskError!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (tasks.isNotEmpty() && activeTasks.isNotEmpty()) {
                    ExposedDropdownMenuBox(
                        expanded = taskDropdownExpanded,
                        onExpandedChange = { taskDropdownExpanded = !taskDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedTask?.title ?: title.ifBlank { "Select Active Task *" },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select Task *") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = taskDropdownExpanded) },
                            modifier = Modifier
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = taskDropdownExpanded,
                            onDismissRequest = { taskDropdownExpanded = false }
                        ) {
                            activeTasks.forEach { task ->
                                DropdownMenuItem(
                                    text = { Text(task.title, style = MaterialTheme.typography.bodyLarge) },
                                    onClick = {
                                        selectedTask = task
                                        title = task.title
                                        if (description.isBlank()) {
                                            description = task.description
                                        }
                                        taskDropdownExpanded = false
                                        showError = false
                                    }
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { 
                        title = it
                        if (it.isNotBlank()) showError = false
                    },
                    label = { Text("Reminder Task / Title *") },
                    singleLine = true,
                    isError = showError,
                    modifier = Modifier.fillMaxWidth()
                )
                if (showError) {
                    Text(
                        text = "⚠️ Task title cannot be empty. Please enter a task to remind about.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description / Notes") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (tasks.isEmpty() && reminderToEdit == null) {
                        taskError = "You must create a task first before adding a reminder."
                        return@TextButton
                    }
                    if (title.isNotBlank()) {
                        val timestamp = reminderToEdit?.reminderTimestamp ?: (System.currentTimeMillis() + (3600000L * 2)) // Default +2 hours
                        onConfirm(title.trim(), description.trim(), timestamp)
                    } else {
                        showError = true
                    }
                }
            ) {
                Text(
                    text = if (reminderToEdit != null) "Save" else "Add",
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
