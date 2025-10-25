package com.example.mobileappdev.data

data class MatchProfile(
    val uid: String,
    val displayName: String,
    val role: String, // "tutor" or "student"
    val subjects: List<String>,
    val level: String, // education level or grade
    val hourlyPrice: Double? = null, // present for tutors
    val city: String? = null,
    val about: String = ""
)
