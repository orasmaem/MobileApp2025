package com.example.mobileappdev.data

import com.example.mobileappdev.data.remote.OpenLibraryApi
import com.example.mobileappdev.data.remote.model.OpenLibraryDoc

class BooksRepository(
    private val api: OpenLibraryApi = OpenLibraryApi.create()
) {
    suspend fun searchBySubject(subject: String, limit: Int = 20): List<Book> {
        val res = api.searchBySubject(subject.trim(), limit)
        return res.docs.mapNotNull { it.toBook() }
    }
}

private fun OpenLibraryDoc.toBook(): Book? {
    val t = title ?: return null
    val authorsJoined = (authorName ?: emptyList()).joinToString().ifBlank { "Unknown author" }
    val id = key ?: "$t|$authorsJoined"
    return Book(
        id = id,
        title = t,
        authors = authorsJoined,
        year = firstPublishYear?.toString(),
        coverUrl = coverUrl("M")
    )
}
