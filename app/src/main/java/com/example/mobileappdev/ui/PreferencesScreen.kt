package com.example.mobileappdev.ui.preferences

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.mobileappdev.ui.common.SimpleTopBar

@Composable
fun PreferencesScreen(onBackClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Top bar
        SimpleTopBar(title = "Edit preferences", onBackClick = onBackClick)

        // Content
        Text(
            text = "Edit preferences",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
