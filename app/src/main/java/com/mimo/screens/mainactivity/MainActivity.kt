package com.mimo.screens.mainactivity

import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.mimo.R
import com.mimo.data.models.Lesson
import com.mimo.data.models.LessonContent
import com.mimo.data.models.LessonInput
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        observeLiveData()
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
            it.lessons
        })

        viewModel.showErrorLiveData.observe(this, {
            showSnackbar(it.errorMessage)
        })

        viewModel.showProgressLiveData.observe(this, {
            showProgressBar(it)
        })
    }

    private fun addLessonToLayout(lesson: Lesson) {
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
                //addTexViewToLayout(LessonContent(content.color, remainingString.substring(0, endInputIndex)))
                remainingString = remainingString.substring(endInputIndex, remainingString.length )
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
        inputEt.layoutParams = LinearLayout.LayoutParams(
            resources.getDimension(R.dimen.et_width_default).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lessonContentFl.addView(inputEt)
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