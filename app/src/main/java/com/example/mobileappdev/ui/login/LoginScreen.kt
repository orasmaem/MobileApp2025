package com.example.mobileappdev.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    role: String,
    onLoginClick: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome, $role!")
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onLoginClick) {
            Text("Log In")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onCreateAccountClick) {
            Text("Create an Account")
        }
    }
}
