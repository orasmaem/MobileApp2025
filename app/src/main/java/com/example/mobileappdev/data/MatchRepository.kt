package com.example.mobileappdev.data

import kotlinx.coroutines.flow.Flow

interface MatchRepository {
    fun streamCandidates(
        currentUserRole: String,
        subject: String?,
        maxPrice: Double?
    ): Flow<List<MatchProfile>>

    suspend fun like(targetUid: String)
    suspend fun unlike(targetUid: String)
    fun likes(): Flow<Set<String>>           // who I liked
    fun matches(): Flow<Set<String>>         // confirmed matches
}
