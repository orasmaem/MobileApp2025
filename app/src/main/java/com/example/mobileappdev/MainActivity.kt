package com.example.mobileappdev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.mobileappdev.ui.theme.MobileAppDevTheme
import com.example.mobileappdev.ui.home.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileAppDevTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(
                        onMatchClick = { /* TODO: navigate to Match screen */ },
                        onTimetableClick = { /* TODO: navigate to Timetable */ },
                        onPreferencesClick = { /* TODO: navigate to Preferences */ }
                    )
                }
            }
        }
    }
}
