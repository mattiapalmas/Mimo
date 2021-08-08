package com.mimo.data.repos.currentlesson

import com.mimo.data.models.CompletedLesson

interface CompletedLessonsDataSource {
    suspend fun getCompletedLessons(): List<CompletedLesson>
    suspend fun saveCompletedLesson(completedLesson: CompletedLesson)
}