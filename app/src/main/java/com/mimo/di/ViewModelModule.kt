package com.mimo.di

import com.mimo.data.repos.currentlesson.CompletedLessonsRepo
import com.mimo.data.repos.lessons.LessonsRepo
import com.mimo.screens.mainactivity.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get() as LessonsRepo, get() as CompletedLessonsRepo) }
}