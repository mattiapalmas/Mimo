package com.mimo.screens.mainactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mimo.common.errors.GeneralError
import com.mimo.data.models.CompletedLesson
import com.mimo.data.models.Lesson
import com.mimo.data.models.Lessons
import com.mimo.data.repos.currentlesson.CompletedLessonsRepo
import com.mimo.data.repos.lessons.LessonsDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.wait

class MainViewModel(
    private val lessonsRepo: LessonsDataSource,
    private val completedLessonsRepo: CompletedLessonsRepo
) : ViewModel() {

    val lessonsLiveData: LiveData<Lessons> get() = lessonsMutableLiveData
    private val lessonsMutableLiveData = MutableLiveData<Lessons>()

    val showErrorLiveData: LiveData<GeneralError> get() = showErrorMutableLiveData
    private val showErrorMutableLiveData = MutableLiveData<GeneralError>()

    private val _showProgressLiveData = MutableLiveData<Boolean>()
    val showProgressLiveData: LiveData<Boolean>
        get() = _showProgressLiveData

    val completedLessonsLiveData: LiveData<List<CompletedLesson>> get() = completedLessonsMutableLiveData
    private val completedLessonsMutableLiveData = MutableLiveData<List<CompletedLesson>>()

    var inputAnswer = ""
    var currentLessonIndex = 0
    var currentLesson: Lesson? = null

    fun loadLessons() {
        showProgressBar()

        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                lessonsRepo.getLessons()
            }
                .onSuccess { result ->
                    hideProgressBar()
                    lessonsMutableLiveData.postValue(result)
                }
                .onFailure { error ->
                    hideProgressBar()
                    showErrorMutableLiveData.postValue(
                        GeneralError(
                            error.message ?: "Something went wrong while fetching posts.",
                            error
                        )
                    )
                }
        }
    }

    fun saveCompletedLesson(completedLesson: CompletedLesson) {
        viewModelScope.launch(Dispatchers.IO) {
            completedLessonsRepo.saveCompletedLesson(completedLesson)
        }
    }

    fun getCompletedLessons() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                completedLessonsRepo.getCompletedLessons()
            }
                .onSuccess { result ->
                    hideProgressBar()
                    completedLessonsMutableLiveData.postValue(result)
                }
        }
    }

    private fun showProgressBar() {
        _showProgressLiveData.postValue(true)
    }

    private fun hideProgressBar() {
        _showProgressLiveData.postValue(false)
    }
}