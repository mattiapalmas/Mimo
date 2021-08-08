package com.mimo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mimo.data.models.CompletedLesson
import com.mimo.data.repos.currentlesson.CompletedLessonsDao

@Database(entities = [CompletedLesson::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun completedLessonsDao(): CompletedLessonsDao
}