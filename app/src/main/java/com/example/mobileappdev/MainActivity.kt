package com.example.mobileappdev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mobileappdev.ui.home.HomeScreen
import com.example.mobileappdev.ui.login.LoginScreen
import com.example.mobileappdev.ui.role.RoleSelectionScreen
import com.example.mobileappdev.ui.MatchScreen
import com.example.mobileappdev.ui.preferences.PreferencesScreen
import com.example.mobileappdev.ui.timetable.TimetableScreen
import com.example.mobileappdev.ui.profile.ProfileSetupScreen
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
                        // Choose Role
                        composable("role") {
                            RoleSelectionScreen(
                                onRoleSelected = { selectedRole ->
                                    navController.navigate("login/$selectedRole")
                                }
                            )
                        }

                        // Login
                        composable("login/{role}") { backStackEntry ->
                            val role = backStackEntry.arguments?.getString("role") ?: "student"
                            LoginScreen(
                                role = role,
                                onLoginClick = {
                                    navController.navigate("home/$role") {
                                        popUpTo("role") { inclusive = true } // clear onboarding
                                    }
                                },
                                onCreateAccountClick = {
                                    navController.navigate("profileSetup/$role?edit=false")
                                }
                            )
                        }

                        // Home
                        composable("home/{role}") { backStackEntry ->
                            val role = backStackEntry.arguments?.getString("role") ?: "student"
                            HomeScreen(
                                role = role,
                                onMatchClick = { navController.navigate("match/$role") },
                                onTimetableClick = { navController.navigate("timetable/$role") },
                                onPreferencesClick = { navController.navigate("profileSetup/$role?edit=true") },
                                onLogoutClick = {navController.navigate("role") {popUpTo("role") { inclusive =true} } }



                            )
                        }

                        // Profile Setup / Edit Profile
                        composable(
                            route = "profileSetup/{role}?edit={edit}",
                            arguments = listOf(
                                navArgument("role") { type = NavType.StringType },
                                navArgument("edit") { type = NavType.BoolType; defaultValue = false }
                            )
                        ) { backStackEntry ->
                            val role = backStackEntry.arguments?.getString("role") ?: "student"
                            val isEditing = backStackEntry.arguments?.getBoolean("edit") ?: false

                            ProfileSetupScreen(
                                role = role,
                                isEditing = isEditing,
                                onSaveProfile = {
                                    navController.navigate("home/$role") {
                                        popUpTo("role") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // Match Screen
                        composable("match/{role}") { backStackEntry ->
                            val role = backStackEntry.arguments?.getString("role") ?: "student"
                            MatchScreen(
                                role = role,
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        // Timetable Screen
                        composable("timetable/{role}") { backStackEntry ->
                            val role = backStackEntry.arguments?.getString("role") ?: "student"
                            TimetableScreen(
                                role = role,
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        // Preferences Screen (legacy route, optional)
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
