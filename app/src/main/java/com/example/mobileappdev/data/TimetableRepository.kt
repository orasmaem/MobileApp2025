package com.example.mobileappdev.data

import kotlinx.coroutines.flow.Flow

interface TimetableRepository {
    fun upcoming(role: String): Flow<List<Lesson>>
    fun past(role: String): Flow<List<Lesson>>
    suspend fun add(role: String, lesson: Lesson)
    suspend fun cancel(id: String)
    suspend fun complete(id: String)
}
