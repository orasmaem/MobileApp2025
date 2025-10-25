package com.example.mobileappdev.ui.timetable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileappdev.data.FakeTimetableRepository
import com.example.mobileappdev.data.Lesson
import com.example.mobileappdev.data.TimetableRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

data class TimetableUiState(
    val tabIndex: Int = 0, // 0 upcoming, 1 past
    val upcoming: List<Lesson> = emptyList(),
    val past: List<Lesson> = emptyList(),
    val showAddDialog: Boolean = false,
)

class TimetableViewModel(
    private val role: String,
    private val repo: TimetableRepository = FakeTimetableRepository()
) : ViewModel() {

    private val _tab = MutableStateFlow(0)
    private val _dialog = MutableStateFlow(false)

    val ui: StateFlow<TimetableUiState> =
        combine(
            repo.upcoming(role),
            repo.past(role),
            _tab, _dialog
        ) { up, pa, tab, dialog ->
            TimetableUiState(tab, up, pa, dialog)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, TimetableUiState())

    fun setTab(index: Int) { _tab.value = index }
    fun openAdd() { _dialog.value = true }
    fun closeAdd() { _dialog.value = false }

    fun addQuick(subject: String, withName: String, date: LocalDate, start: LocalTime, minutes: Int, price: Double?) {
        val startDt = LocalDateTime.of(date, start)
        val endDt = startDt.plusMinutes(minutes.toLong())
        val lesson = Lesson(
            id = UUID.randomUUID().toString(),
            subject = subject,
            withName = withName,
            start = startDt,
            end = endDt,
            pricePerHour = price
        )
        viewModelScope.launch {
            repo.add(role, lesson)
            _dialog.value = false
        }
    }

    fun cancel(id: String) = viewModelScope.launch { repo.cancel(id) }
    fun complete(id: String) = viewModelScope.launch { repo.complete(id) }
}
