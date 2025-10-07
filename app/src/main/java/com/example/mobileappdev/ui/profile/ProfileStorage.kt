package com.example.mobileappdev.ui.profile

import android.content.Context
import androidx.compose.runtime.mutableStateMapOf

object ProfileStorage {

    private const val PREF_NAME = "profile_prefs"
    private const val KEY_ROLE = "role"
    private const val KEY_GRADE = "grade"
    private const val KEY_SUBJECTS = "subjects"
    private const val KEY_ABOUT = "about"

    fun saveProfile(context: Context, role: String, grade: String, subjects: Map<String, Boolean>, about: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString("$KEY_ROLE", role)
            putString("$KEY_GRADE", grade)
            putString("$KEY_ABOUT", about)
            // Save subjects
            val selectedSubjects = subjects.filter { it.value }.keys.joinToString(",")
            putString("$KEY_SUBJECTS", selectedSubjects)
            apply()
        }
    }

    fun loadProfile(context: Context, role: String): Triple<String, Map<String, Boolean>, String> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val grade = prefs.getString("$KEY_GRADE", "") ?: ""
        val about = prefs.getString("$KEY_ABOUT", "") ?: ""
        val subjectString = prefs.getString("$KEY_SUBJECTS", "") ?: ""
        val selectedSubjects = mutableStateMapOf<String, Boolean>()
        if (subjectString.isNotEmpty()) {
            subjectString.split(",").forEach { subject ->
                selectedSubjects[subject] = true
            }
        }
        return Triple(grade, selectedSubjects, about)
    }
}
