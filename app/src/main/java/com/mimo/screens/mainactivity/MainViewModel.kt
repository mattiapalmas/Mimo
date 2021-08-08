package com.mimo.screens.mainactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mimo.common.errors.GeneralError
import com.mimo.data.models.Lessons
import com.mimo.data.repos.lessons.LessonsDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val lessonsRepo: LessonsDataSource
) : ViewModel() {

    val lessonsLiveData: LiveData<Lessons> get() = lessonsMutableLiveData
    private val lessonsMutableLiveData = MutableLiveData<Lessons>()

    val showErrorLiveData: LiveData<GeneralError> get() = showErrorMutableLiveData
    private val showErrorMutableLiveData = MutableLiveData<GeneralError>()

    private val _showProgressLiveData = MutableLiveData<Boolean>()
    val showProgressLiveData: LiveData<Boolean>
        get() = _showProgressLiveData

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

    private fun showProgressBar() {
        _showProgressLiveData.postValue(true)
    }

    private fun hideProgressBar() {
        _showProgressLiveData.postValue(false)
    }
}