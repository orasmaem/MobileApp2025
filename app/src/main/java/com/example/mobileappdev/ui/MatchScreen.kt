package com.example.mobileappdev.ui.match

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.mobileappdev.ui.common.SimpleTopBar

@Composable
fun MatchScreen(onBackClick: () -> Unit, role: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        SimpleTopBar(title = "Match with Tutors", onBackClick = onBackClick)

        Text(
            text = "Match screen",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
