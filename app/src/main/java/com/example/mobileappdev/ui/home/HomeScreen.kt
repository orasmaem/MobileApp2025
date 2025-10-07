package com.example.mobileappdev.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HomeScreen(
    role: String = "student",
    onMatchClick: () -> Unit,
    onTimetableClick: () -> Unit,
    onPreferencesClick: () -> Unit
) {
    Scaffold(
        topBar = { /* your custom top bar here */ }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (role == "student") {
                Button(
                    onClick = onMatchClick,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) { Text("Match with tutors") }

                Button(
                    onClick = onTimetableClick,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) { Text("My timetable") }

                Button(
                    onClick = onPreferencesClick,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) { Text("Edit preferences") }
            } else {
                Button(
                    onClick = onMatchClick,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) { Text("Connect with students") }

                Button(
                    onClick = onTimetableClick,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) { Text("My timetable") }

                Button(
                    onClick = onPreferencesClick,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) { Text("Edit my qualifications") }
            }
        }
    }
}


@Composable
private fun HomeTopBar(onProfileClick: () -> Unit) {
    Surface(shadowElevation = 4.dp) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Home",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterStart)
            )
            IconButton(
                onClick = onProfileClick,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(onMatchClick = {}, onTimetableClick = {}, onPreferencesClick = {})
}
