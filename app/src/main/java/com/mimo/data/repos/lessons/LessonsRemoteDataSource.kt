package com.mimo.data.repos.lessons

import com.mimo.data.api.MimoApi
import com.mimo.data.models.Lessons

class LessonsRemoteDataSource(
    private val mimoApi: MimoApi
) : LessonsDataSource {

    override suspend fun getLessons(): Lessons {
        return mimoApi.getLessons()
    }
}