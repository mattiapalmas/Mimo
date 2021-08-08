package com.mimo.data.repos.currentlesson

import com.mimo.data.models.CompletedLesson


class CompletedLessonsRoomDataSource(
    private val dao: CompletedLessonsDao
) : CompletedLessonsDataSource {


    override suspend fun getCompletedLessons(): List<CompletedLesson> {
        return dao.get()
    }

    override suspend fun saveCompletedLesson(completedLesson: CompletedLesson) {
        dao.save(completedLesson)
    }
}