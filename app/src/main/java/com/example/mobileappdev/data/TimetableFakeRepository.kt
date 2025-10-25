package com.example.mobileappdev.data

import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import java.util.*

class FakeTimetableRepository : TimetableRepository {

    // One shared list for demo
    private val all = MutableStateFlow<List<Lesson>>(seed())

    override fun upcoming(role: String): Flow<List<Lesson>> =
        all.map { list -> list.filter { it.start.isAfter(LocalDateTime.now().minusMinutes(1)) } }
            .map { it.sortedBy { l -> l.start } }

    override fun past(role: String): Flow<List<Lesson>> =
        all.map { list -> list.filter { it.end.isBefore(LocalDateTime.now()) } }
            .map { it.sortedByDescending { l -> l.start } }

    override suspend fun add(role: String, lesson: Lesson) {
        all.value = all.value + lesson
    }

    override suspend fun cancel(id: String) {
        all.value = all.value.map { if (it.id == id) it.copy(status = Lesson.Status.Cancelled) else it }
    }

    override suspend fun complete(id: String) {
        all.value = all.value.map { if (it.id == id) it.copy(status = Lesson.Status.Completed) else it }
    }

    private fun seed(): List<Lesson> {
        val now = LocalDateTime.now()
        return listOf(
            Lesson(UUID.randomUUID().toString(), "Math", "Ava Li", now.plusHours(2), now.plusHours(3), 25.0),
            Lesson(UUID.randomUUID().toString(), "English", "Marko Saar", now.plusDays(1).withHour(10).withMinute(0), now.plusDays(1).withHour(11), 18.0),
            Lesson(UUID.randomUUID().toString(), "Chemistry", "Helena", now.minusDays(1).withHour(15), now.minusDays(1).withHour(16), 30.0, Lesson.Status.Completed)
        )
    }
}
