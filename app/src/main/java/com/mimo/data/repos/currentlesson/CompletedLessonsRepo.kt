package com.mimo.data.repos.currentlesson

import com.mimo.data.models.CompletedLesson

class CompletedLessonsRepo(
    private val localDataSource: CompletedLessonsDataSource
): CompletedLessonsDataSource {

    override suspend fun getCompletedLessons(): List<CompletedLesson> {
        return localDataSource.getCompletedLessons()
    }

    override suspend fun saveCompletedLesson(completedLesson: CompletedLesson) {
        localDataSource.saveCompletedLesson(completedLesson)
    }
}