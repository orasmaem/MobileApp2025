package com.example.mobileappdev.ui.match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileappdev.data.FakeMatchRepository
import com.example.mobileappdev.data.MatchProfile
import com.example.mobileappdev.data.MatchRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class MatchFilters(
    val subject: String? = null,
    val maxPrice: Double? = null // tutors only
)

data class MatchUiState(
    val loading: Boolean = true,
    val profiles: List<MatchProfile> = emptyList(),
    val liked: Set<String> = emptySet(),
    val matches: Set<String> = emptySet(),
    val filters: MatchFilters = MatchFilters()
)

class MatchViewModel(
    private val role: String,
    private val repo: MatchRepository = FakeMatchRepository() // swap to DI/Firebase later
) : ViewModel() {

    private val _filters = MutableStateFlow(MatchFilters())
    private val _profiles = _filters.flatMapLatest { f ->
        repo.streamCandidates(currentUserRole = role, subject = f.subject, maxPrice = f.maxPrice)
    }

    val ui: StateFlow<MatchUiState> =
        combine(_profiles, repo.likes(), repo.matches(), _filters) { list, liked, matches, filters ->
            MatchUiState(
                loading = false,
                profiles = list,
                liked = liked,
                matches = matches,
                filters = filters
            )
        }.stateIn(viewModelScope, SharingStarted.Eagerly, MatchUiState())

    fun setSubject(subject: String?) = viewModelScope.launch {
        _filters.update { it.copy(subject = subject) }
    }

    fun setMaxPrice(max: Double?) = viewModelScope.launch {
        _filters.update { it.copy(maxPrice = max) }
    }

    fun toggleLike(uid: String) = viewModelScope.launch {
        if (ui.value.liked.contains(uid)) repo.unlike(uid) else repo.like(uid)
    }
}
