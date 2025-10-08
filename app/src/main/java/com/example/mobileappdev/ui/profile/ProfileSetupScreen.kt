package com.example.mobileappdev.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun ProfileSetupScreen(
    role: String,
    isEditing: Boolean = false,
    onSaveProfile: () -> Unit
) {
    val context = LocalContext.current
    val subjectsList = listOf("Math", "Physics", "Chemistry", "Biology", "English", "History")

    // Load previously saved data (if editing)
    val (savedGrade, savedSubjects, savedAbout) = if (isEditing)
        ProfileStorage.loadProfile(context, role)
    else
        Triple("", mutableMapOf<String, Boolean>(), "")

    var selectedDropdown by remember { mutableStateOf(savedGrade) }
    var aboutText by remember { mutableStateOf(TextFieldValue(savedAbout)) }
    val selectedSubjects = remember { mutableStateMapOf<String, Boolean>().apply { putAll(savedSubjects) } }

    var expanded by remember { mutableStateOf(false) }

    val dropdownOptions = if (role == "student")
        listOf("Elementary", "Middle School", "High School", "University")
    else
        listOf("Bachelor", "Master", "PhD")

    val isFormValid by remember {
        derivedStateOf {
            selectedDropdown.isNotEmpty() &&
                    selectedSubjects.values.any { it } &&
                    aboutText.text.isNotBlank()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = if (isEditing) "Edit Profile" else "Create Profile",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Scrollable content
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Dropdown
            item {
                Text(
                    text = if (role == "student") "Select your grade:" else "Select level of education:",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(4.dp))

                Box {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = if (selectedDropdown.isEmpty()) "Choose..." else selectedDropdown)
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        dropdownOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedDropdown = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Subjects section
            item {
                Text(
                    text = if (role == "student")
                        "What do you need help with?"
                    else
                        "Which subjects would you like to teach?",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            items(subjectsList) { subject ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = selectedSubjects[subject] ?: false,
                        onCheckedChange = { checked ->
                            selectedSubjects[subject] = checked
                        }
                    )
                    Text(text = subject)
                }
            }

            // About section
            item {
                Text(
                    text = "Tell us more about yourself:",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = aboutText,
                    onValueChange = { aboutText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    placeholder = { Text("Write something about yourself...") }
                )
            }
        }

        // "All fields required" note
        if (!isFormValid) {
            Text(
                text = "* Please fill in all required fields before continuing.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Save / Create button (enabled only when valid)
        Button(
            onClick = {
                ProfileStorage.saveProfile(
                    context = context,
                    role = role,
                    grade = selectedDropdown,
                    subjects = selectedSubjects,
                    about = aboutText.text
                )
                onSaveProfile()
            },
            enabled = isFormValid, // disable until valid
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = if (isEditing) "Save" else "Create Profile")
        }
    }
}
