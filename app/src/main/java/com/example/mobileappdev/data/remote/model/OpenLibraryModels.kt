package com.example.mobileappdev.data.remote.model

import com.squareup.moshi.Json

// We only map the fields we actually use.
data class OpenLibrarySearchResponse(
    @Json(name = "numFound") val numFound: Int? = null,
    @Json(name = "docs") val docs: List<OpenLibraryDoc> = emptyList()
)

data class OpenLibraryDoc(
    @Json(name = "key") val key: String? = null,
    @Json(name = "title") val title: String? = null,
    @Json(name = "author_name") val authorName: List<String>? = null,
    @Json(name = "first_publish_year") val firstPublishYear: Int? = null,
    @Json(name = "cover_i") val coverId: Int? = null
) {
    fun coverUrl(size: String = "M"): String? =
        coverId?.let { "https://covers.openlibrary.org/b/id/${it}-${size}.jpg" }
}
