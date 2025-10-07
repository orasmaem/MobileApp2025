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
import com.example.mobileappdev.ui.login.LoginScreen
import com.example.mobileappdev.ui.role.RoleSelectionScreen
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
                        startDestination = "role"
                    ) {
                        // Choose role
                        composable("role") {
                            RoleSelectionScreen(
                                onRoleSelected = { selectedRole ->
                                    navController.navigate("login/$selectedRole")
                                }
                            )
                        }

                        //Login
                        composable("login/{role}") { backStackEntry ->
                            val role = backStackEntry.arguments?.getString("role") ?: "student"
                            LoginScreen(role = role) {
                                navController.navigate("home/$role") {
                                    popUpTo("role") { inclusive = true } // clear previous flow
                                }
                            }
                        }

                        // Home
                        composable("home/{role}") { backStackEntry ->
                            val role = backStackEntry.arguments?.getString("role") ?: "student"
                            HomeScreen(
                                role = role,
                                onMatchClick = { navController.navigate("match/$role") },
                                onTimetableClick = { navController.navigate("timetable/$role") },
                                onPreferencesClick = { navController.navigate("preferences/$role") }
                            )
                        }

                        // Match screen
                        composable("match/{role}") { backStackEntry ->
                            val role = backStackEntry.arguments?.getString("role") ?: "student"
                            MatchScreen(
                                role = role,
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        // Timetable screen
                        composable("timetable/{role}") { backStackEntry ->
                            val role = backStackEntry.arguments?.getString("role") ?: "student"
                            TimetableScreen(
                                role = role,
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        // Preferences screen
                        composable("preferences/{role}") { backStackEntry ->
                            val role = backStackEntry.arguments?.getString("role") ?: "student"
                            PreferencesScreen(
                                role = role,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
