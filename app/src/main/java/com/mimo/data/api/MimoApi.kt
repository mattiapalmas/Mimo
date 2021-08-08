package com.mimo.data.api

import com.mimo.data.models.Lessons
import retrofit2.http.GET

interface MimoApi {

    @GET("lessons")
    suspend fun getLessons(): Lessons
}