package com.example.mobileappdev.ui.role

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RoleSelectionScreen(onRoleSelected: (String) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Are you a student or a tutor?",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            Button(
                onClick = { onRoleSelected("student") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Text("Student")
            }
            Button(
                onClick = { onRoleSelected("tutor") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Text("Tutor")
            }
        }
    }
}
