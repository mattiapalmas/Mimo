package com.mimo.di

import androidx.room.Room
import com.mimo.common.Constants.DATABASE_NAME
import com.mimo.data.AppDatabase
import com.mimo.data.repos.currentlesson.CurrentLessonIndexRepo
import com.mimo.data.repos.currentlesson.CurrentLessonIndexRoomDataSource
import com.mimo.data.repos.lessons.LessonsRemoteDataSource
import com.mimo.data.repos.lessons.LessonsRepo
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataSourceModule = module {

    single {
        Room.databaseBuilder(androidApplication(), AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDatabase>().currentLessonIndexDao() }

    single { LessonsRemoteDataSource(get()) }
    single { LessonsRepo(get() as LessonsRemoteDataSource) }

    single { CurrentLessonIndexRoomDataSource(get()) }
    single { CurrentLessonIndexRepo(get() as CurrentLessonIndexRoomDataSource) }

}