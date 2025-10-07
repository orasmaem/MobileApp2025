package com.example.mobileappdev.ui.timetable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.mobileappdev.ui.common.SimpleTopBar

@Composable
fun TimetableScreen(onBackClick: () -> Unit, role: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        SimpleTopBar(title = "Timetable", onBackClick = onBackClick)

        Text(
            text = "Timetable",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
