package com.example.mobileappdev.data.remote

import com.example.mobileappdev.data.remote.model.OpenLibrarySearchResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

interface OpenLibraryApi {
    @GET("search.json")
    suspend fun searchBySubject(
        @Query("subject") subject: String,
        @Query("limit") limit: Int = 20
    ): OpenLibrarySearchResponse

    companion object {
        fun create(): OpenLibraryApi {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())   // <-- required for Kotlin data classes
                .build()

            return Retrofit.Builder()
                .baseUrl("https://openlibrary.org/")  // trailing slash required
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(OpenLibraryApi::class.java)
        }
    }
}
