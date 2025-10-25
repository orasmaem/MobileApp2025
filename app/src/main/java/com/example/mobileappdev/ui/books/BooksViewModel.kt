package com.example.mobileappdev.ui.books

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileappdev.data.Book
import com.example.mobileappdev.data.BooksRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface BooksState {
    object Idle : BooksState
    object Loading : BooksState
    data class Success(val items: List<Book>) : BooksState
    data class Error(val message: String) : BooksState
}

class BooksViewModel(
    private val repo: BooksRepository = BooksRepository()
) : ViewModel() {

    var state: BooksState = BooksState.Idle
        private set

    fun load(subject: String, onState: (BooksState) -> Unit) {
        state = BooksState.Loading
        onState(state)
        viewModelScope.launch {
            try {
                val items = repo.searchBySubject(subject, 20)
                state = BooksState.Success(items)
                onState(state)
            } catch (ce: CancellationException) { throw ce }
            catch (e: IOException) {
                Log.e("BooksVM", "Network error", e)
                state = BooksState.Error("No internet connection.")
                onState(state)
            } catch (e: HttpException) {
                Log.e("BooksVM", "HTTP error", e)
                state = BooksState.Error("Server error: ${e.code()}")
                onState(state)
            } catch (e: Exception) {
                Log.e("BooksVM", "Unexpected", e)
                state = BooksState.Error("Unexpected error: ${e.localizedMessage ?: "Unknown"}")
                onState(state)
            }
        }
    }
}
