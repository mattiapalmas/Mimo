package com.mimo.data.repos.lessons

import com.mimo.data.models.Lessons

class LessonsRepo(
    private val remoteDataSource: LessonsDataSource
) : LessonsDataSource {

    override suspend fun getLessons(): Lessons {
        return remoteDataSource.getLessons()
    }
}