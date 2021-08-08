package com.mimo.data.repos.lessons

import com.mimo.data.models.Lessons

interface LessonsDataSource {

    suspend fun getLessons(): Lessons
}