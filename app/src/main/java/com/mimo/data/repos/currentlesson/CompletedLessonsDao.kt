package com.mimo.data.repos.currentlesson

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mimo.data.models.CompletedLesson

@Dao
interface CompletedLessonsDao {

    @Query("SELECT * FROM completedLessons")
    suspend fun get(): List<CompletedLesson>

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun save(completedLesson: CompletedLesson)
}