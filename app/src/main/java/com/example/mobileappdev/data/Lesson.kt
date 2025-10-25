package com.example.mobileappdev.data

import java.time.LocalDateTime
import java.time.Duration

data class Lesson(
    val id: String,
    val subject: String,
    val withName: String,         // counterparty: tutor name (if student) or student name (if tutor)
    val start: LocalDateTime,
    val end: LocalDateTime,
    val pricePerHour: Double? = null, // tutors may see this; optional for students
    val status: Status = Status.Confirmed,
) {
    enum class Status { Pending, Confirmed, Completed, Cancelled }

    val durationMinutes: Long get() = Duration.between(start, end).toMinutes()
}
