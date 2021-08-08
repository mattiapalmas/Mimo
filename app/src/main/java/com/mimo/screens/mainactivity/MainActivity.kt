package com.mimo.screens.mainactivity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.contains
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.mimo.R
import com.mimo.common.Constants.INPUT_ET_ID
import com.mimo.data.models.CompletedLesson
import com.mimo.data.models.Lesson
import com.mimo.data.models.LessonContent
import com.mimo.data.models.LessonInput
import com.mimo.screens.done.DoneActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()
    private var lessonStartedAt: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        observeLiveData()
        setupNextBtnListener()
        viewModel.loadLessons()
    }

    private fun observeLiveData() {
        viewModel.lessonsLiveData.observe(this, {
            if (it.lessons.isEmpty()) {
                showSnackbar(getString(R.string.error_no_lessons_available))
                return@observe
            }

            titleTv.text = getString(
                R.string.lessons_title_with_count,
                "1",
                it.lessons.size.toString()
            )
            addLessonToLayout(it.lessons[0])
        })

        viewModel.showErrorLiveData.observe(this, {
            showSnackbar(it.errorMessage)
        })

        viewModel.showProgressLiveData.observe(this, {
            showProgressBar(it)
        })

        viewModel.completedLessonsLiveData.observe(this, {
            // Left it here in case you need to check if they are properly saved.
            // No UI was requested for showing the completed lessons.
        })
    }

    private fun setupNextBtnListener() {
        nextBtn.setOnClickListener {
            val inputEt = lessonContentFl.findViewById<EditText>(INPUT_ET_ID)

            if (inputEt == null || inputEt.text.toString() == viewModel.inputAnswer) {
                viewModel.currentLesson?.let {
                    viewModel.saveCompletedLesson(CompletedLesson(it.id, lessonStartedAt, System.currentTimeMillis()))
                }
                loadNextLesson()
            } else {
                showSnackbar(getString(R.string.error_wrong_answer))
            }
        }
    }

    private fun addLessonToLayout(lesson: Lesson) {
        viewModel.currentLesson = lesson
        viewModel.currentLessonIndex++
        lessonStartedAt = System.currentTimeMillis()
        lessonContentFl.removeAllViews()

        var currentContentIndex = 0

        lesson.content.forEach { content ->
            when {
                lesson.input == null -> {
                    addTexViewToLayout(content)
                }
                isContentContainingAnInput(content, lesson.input, currentContentIndex) -> {
                    addFieldsWithInput(content, lesson.input, currentContentIndex)
                }
                else -> {
                    addTexViewToLayout(content)
                }
            }

            currentContentIndex += content.text.length
        }
    }

    private fun isContentContainingAnInput(
        content: LessonContent,
        lessonInput: LessonInput,
        contentStartIndex: Int
    ): Boolean {
        val contentLastIndex = contentStartIndex + (content.text.length -1)

        return lessonInput.startIndex in contentStartIndex until contentLastIndex
    }

    private fun addFieldsWithInput(
        content: LessonContent,
        lessonInput: LessonInput,
        currentContentIndex: Int
    ) {
        var startInputIndex = lessonInput.startIndex - currentContentIndex
        var endInputIndex = lessonInput.endIndex - currentContentIndex
        var remainingString = content.text
        var inputFound = false

        while (remainingString.isNotBlank()) {
            if (startInputIndex == 0 && !inputFound) {
                addEditTextToLayout()
                viewModel.inputAnswer = remainingString.substring(0, endInputIndex)
                remainingString = remainingString.substring(endInputIndex, remainingString.length)
                inputFound = true
            } else {
                var textToShow: String

                if (inputFound) {
                    textToShow = remainingString
                    remainingString = ""
                } else {
                    textToShow = remainingString.substring(0, startInputIndex)
                    remainingString = remainingString.substring(startInputIndex, remainingString.lastIndex )
                    startInputIndex = 0
                    endInputIndex -= startInputIndex
                }
                addTexViewToLayout(LessonContent(content.color, textToShow))

            }
        }
    }

    private fun addTexViewToLayout(content: LessonContent) {
        val contentTv = TextView(this)
        contentTv.text = content.text
        contentTv.setTextColor(Color.parseColor(content.color))
        contentTv.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lessonContentFl.addView(contentTv)
    }

    private fun addEditTextToLayout() {
        val inputEt = EditText(this)
        inputEt.id = INPUT_ET_ID
        inputEt.isSingleLine = true
        inputEt.imeOptions = EditorInfo.IME_ACTION_DONE
        inputEt.layoutParams = LinearLayout.LayoutParams(
            resources.getDimension(R.dimen.et_width_default).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lessonContentFl.addView(inputEt)
    }

    private fun loadNextLesson() {
        val lessons = viewModel.lessonsLiveData.value?.lessons
        val lessonsCount = lessons?.size ?: 0

        if (viewModel.currentLessonIndex < lessonsCount && lessons != null) {
            addLessonToLayout(lessons[viewModel.currentLessonIndex])
        } else {
            goToDoneScreen()
        }
    }

    private fun goToDoneScreen() {
        val intent = Intent(this, DoneActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun showSnackbar(error: String) {
        Snackbar.make(main_layout, error, Snackbar.LENGTH_LONG)
            .setDuration(Snackbar.LENGTH_LONG)
            .show()
    }

    private fun showProgressBar(isShown: Boolean) {
        progress_bar.isVisible = isShown
    }
}