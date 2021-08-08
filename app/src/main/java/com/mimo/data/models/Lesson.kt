package com.mimo.data.models

data class Lesson(
    val id: Int,
    val content: List<LessonContent>,
    val input: LessonInput?
)