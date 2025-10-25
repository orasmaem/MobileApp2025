package com.example.mobileappdev.ui.timetable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mobileappdev.data.Lesson
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableScreen(onBackClick: () -> Unit, role: String) {
    val vm = remember(role) { TimetableViewModel(role) }
    val ui by vm.ui.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Timetable") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = vm::openAdd) { Icon(Icons.Filled.Add, contentDescription = "Add") }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(selectedTabIndex = ui.tabIndex) {
                Tab(selected = ui.tabIndex == 0, onClick = { vm.setTab(0) }, text = { Text("Upcoming") })
                Tab(selected = ui.tabIndex == 1, onClick = { vm.setTab(1) }, text = { Text("Past") })
            }

            val list = if (ui.tabIndex == 0) ui.upcoming else ui.past

            if (list.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No lessons.")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(list, key = { it.id }) { lesson ->
                        LessonCard(
                            lesson = lesson,
                            role = role,
                            onCancel = { vm.cancel(lesson.id) },
                            onComplete = { vm.complete(lesson.id) }
                        )
                    }
                }
            }
        }
    }

    if (ui.showAddDialog) {
        AddLessonDialog(
            role = role,
            onDismiss = vm::closeAdd,
            onConfirm = { subject, withName, date, time, minutes, price ->
                vm.addQuick(subject, withName, date, time, minutes, price)
            }
        )
    }
}

@Composable
private fun LessonCard(
    lesson: Lesson,
    role: String,
    onCancel: () -> Unit,
    onComplete: () -> Unit
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(lesson.subject, style = MaterialTheme.typography.titleMedium)
            Text(
                (if (role == "student") "Tutor: " else "Student: ") + lesson.withName,
                style = MaterialTheme.typography.bodyMedium
            )
            val timeFmt = DateTimeFormatter.ofPattern("EEE, d MMM • HH:mm")
            Text("${lesson.start.format(timeFmt)} – ${lesson.end.toLocalTime()}", style = MaterialTheme.typography.bodySmall)

            val statusColor = when (lesson.status) {
                Lesson.Status.Pending -> MaterialTheme.colorScheme.secondary
                Lesson.Status.Confirmed -> MaterialTheme.colorScheme.primary
                Lesson.Status.Completed -> MaterialTheme.colorScheme.tertiary
                Lesson.Status.Cancelled -> MaterialTheme.colorScheme.error
            }
            Text(lesson.status.name, color = statusColor, fontWeight = FontWeight.SemiBold)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (lesson.status == Lesson.Status.Confirmed || lesson.status == Lesson.Status.Pending) {
                    OutlinedButton(onClick = onCancel) { Text("Cancel") }
                }
                if (lesson.status == Lesson.Status.Confirmed) {
                    Button(onClick = onComplete) { Text("Mark done") }
                }
            }
        }
    }
}

@Composable
private fun AddLessonDialog(
    role: String,
    onDismiss: () -> Unit,
    onConfirm: (subject: String, withName: String, date: LocalDate, time: LocalTime, minutes: Int, price: Double?) -> Unit
) {
    var subject by remember { mutableStateOf("") }
    var withName by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now()) }
    var hour by remember { mutableStateOf(10) }
    var minute by remember { mutableStateOf(0) }
    var duration by remember { mutableStateOf(60) }
    var priceText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            val price = priceText.toDoubleOrNull()
            TextButton(
                enabled = subject.isNotBlank() && withName.isNotBlank(),
                onClick = {
                    onConfirm(subject, withName, date, LocalTime.of(hour, minute), duration, price)
                }
            ) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        title = { Text("Add lesson") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(subject, { subject = it }, label = { Text("Subject") })
                OutlinedTextField(
                    withName, { withName = it },
                    label = { Text(if (role == "student") "Tutor name" else "Student name") }
                )
                // Simple date/time pickers without experimental APIs (quick inputs):
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = date.toString(),
                        onValueChange = { runCatching { date = LocalDate.parse(it) } },
                        label = { Text("Date (YYYY-MM-DD)") },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = hour.toString(),
                        onValueChange = { hour = it.toIntOrNull()?.coerceIn(0,23) ?: hour },
                        label = { Text("Hour 0–23") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = minute.toString(),
                        onValueChange = { minute = it.toIntOrNull()?.coerceIn(0,59) ?: minute },
                        label = { Text("Min 0–59") },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = duration.toString(),
                        onValueChange = { duration = it.toIntOrNull()?.coerceIn(15, 240) ?: duration },
                        label = { Text("Duration (min)") },
                        modifier = Modifier.weight(1f)
                    )
                    if (role == "student") {
                        OutlinedTextField(
                            value = priceText,
                            onValueChange = { priceText = it },
                            label = { Text("€/h (optional)") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    )
}
