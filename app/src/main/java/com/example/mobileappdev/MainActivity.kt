package com.example.mobileappdev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobileappdev.ui.home.HomeScreen
import com.example.mobileappdev.ui.match.MatchScreen
import com.example.mobileappdev.ui.preferences.PreferencesScreen
import com.example.mobileappdev.ui.timetable.TimetableScreen
import com.example.mobileappdev.ui.theme.MobileAppDevTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileAppDevTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(
                                onMatchClick = { navController.navigate("match") },
                                onTimetableClick = { navController.navigate("timetable") },
                                onPreferencesClick = { navController.navigate("preferences") }
                            )
                        }
                        composable("match") { MatchScreen(onBackClick = { navController.popBackStack() }) }
                        composable("timetable") { TimetableScreen(onBackClick = { navController.popBackStack() }) }
                        composable("preferences") { PreferencesScreen(onBackClick = { navController.popBackStack() }) }
                    }

                }
            }
        }
    }
}
