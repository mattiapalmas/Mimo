package com.mimo.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "completedLessons")
data class CompletedLesson(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 1,
    val lessonStartedAt: Long,
    val lessonEndedAt: Long
)