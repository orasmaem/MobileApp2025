package com.example.mobileappdev.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

/**
 * Local in-memory repo for MVP/dev.
 * Replace with Firestore later and keep the interface intact.
 */
class FakeMatchRepository : MatchRepository {

    // Seed data
    private val seed = listOf(
        MatchProfile("t1", "Ava Li", "tutor", listOf("Math","Physics"), "Master", 25.0, "Tallinn", "STEM focused."),
        MatchProfile("t2", "Marko Saar", "tutor", listOf("English","History"), "Bachelor", 18.0, "Tartu", "Essay help."),
        MatchProfile("t3", "Julia Novak", "tutor", listOf("Biology","Chemistry"), "PhD", 30.0, "Tallinn", "Exam prep."),
        MatchProfile("s1", "Katrin", "student", listOf("Math"), "High School", null, "Tartu", "Needs algebra support."),
        MatchProfile("s2", "Rasmus", "student", listOf("English"), "University", null, "Tallinn", "Academic writing."),
        MatchProfile("s3", "Helena", "student", listOf("Chemistry"), "High School", null, "Tallinn", "Lab reports.")
    )

    private val likedByMe = MutableStateFlow<MutableSet<String>>(mutableSetOf())
    private val likedByOther = MutableStateFlow<MutableSet<String>>(mutableSetOf("t1","s2")) // pretend some liked me

    override fun streamCandidates(
        currentUserRole: String,
        subject: String?,
        maxPrice: Double?
    ): Flow<List<MatchProfile>> =
        MutableStateFlow(seed).map { all ->
            val targetRole = if (currentUserRole == "student") "tutor" else "student"
            all.filter { it.role == targetRole }
                .filter { subject == null || it.subjects.contains(subject) }
                .filter {
                    if (targetRole == "tutor" && maxPrice != null) (it.hourlyPrice ?: Double.MAX_VALUE) <= maxPrice
                    else true
                }
        }

    override suspend fun like(targetUid: String) {
        likedByMe.value = (likedByMe.value.toMutableSet().apply { add(targetUid) })
    }

    override suspend fun unlike(targetUid: String) {
        likedByMe.value = (likedByMe.value.toMutableSet().apply { remove(targetUid) })
    }

    override fun likes(): Flow<Set<String>> = likedByMe.map { it.toSet() }

    override fun matches(): Flow<Set<String>> =
        combine(likedByMe, likedByOther) { mine, theirs ->
            mine.intersect(theirs)
        }
}
