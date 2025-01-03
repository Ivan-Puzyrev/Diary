package com.example.diary.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.diary.DiaryApp
import com.example.diary.R
import com.example.diary.databinding.ActivityAddTaskBinding
import com.example.diary.domain.Task
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject


class AddTaskActivity : AppCompatActivity() {
    private val component by lazy {
        (application as DiaryApp).component
    }
    private lateinit var binding: ActivityAddTaskBinding
    private var taskList: List<Task> = mutableListOf()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val addTaskViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AddTaskViewModel::class]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getNameEditTextFocusAndShowKeyboard()
        setupNumberPickers()
        observeViewModel()
        setupClickListeners()

    }

    private fun observeViewModel() {
        with(addTaskViewModel) {
            taskListLD.observe(this@AddTaskActivity) {
                taskList = it
            }
            shouldCloseScreen.observe(this@AddTaskActivity) {
                if (it) {
                    finish()
                }
            }
        }
    }

    private fun setupClickListeners() {
        with(binding) {
            saveButton.setOnClickListener {
                val date = intent.getLongExtra(KEY_ADD_TASK_ACTIVITY, 0)
                if (nameEditText.text.toString().trim().isEmpty()) {
                    Toast.makeText(
                        this@AddTaskActivity,
                        getString(R.string.fill_in_the_name_field),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    val dateStart =
                        Instant.ofEpochSecond(date + numberPickerStart.value * 3600.toLong())
                            .atZone(ZoneId.systemDefault())
                    val dateEnd =
                        Instant.ofEpochSecond(date + (numberPickerEnd.value + 1) * 3600.toLong())
                            .atZone(ZoneId.systemDefault())
                    val task = Task(
                        taskList.size,
                        dateStart,
                        dateEnd,
                        nameEditText.text.toString().trim(),
                        descriptionEditText.text.toString().trim()
                    )
                    addTaskViewModel.addTask(task)
                }
            }
        }
    }

    private fun getNameEditTextFocusAndShowKeyboard() {
        binding.nameEditText.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.nameEditText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setupNumberPickers() {
        val itemsTimeStart = Array(24) { "0" }
        for (i in 0..23) {
            val hour = i.toString().padStart(2, '0')
            itemsTimeStart[i] = "$hour:00"
        }
        val itemsTimeEnd = Array(24) { "0" }
        for (i in 0..22) {
            val hour = (i + 1).toString().padStart(2, '0')
            itemsTimeEnd[i] = "$hour:00"
        }
        itemsTimeEnd[23] = "00:00"

        with(binding) {
            numberPickerStart.apply {
                minValue = 0
                maxValue = itemsTimeStart.size - 1
                displayedValues = itemsTimeStart
                value = 12
            }

            numberPickerEnd.apply {
                minValue = 0
                maxValue = itemsTimeEnd.size - 1
                displayedValues = itemsTimeEnd
                value = 12
            }

            numberPickerStart.setOnValueChangedListener { _, _, newValue ->
                if (newValue == 0 && numberPickerEnd.value == 23) {
                    numberPickerEnd.value = 0
                } else {
                    if (newValue >= numberPickerEnd.value) {
                        numberPickerEnd.value = newValue
                    }
                }
            }

            numberPickerEnd.setOnValueChangedListener { _, _, newValue ->
                if (newValue == 23 && numberPickerStart.value == 0) {
                    numberPickerStart.value = 23
                } else {
                    if (newValue <= numberPickerStart.value) {
                        numberPickerStart.value = newValue
                    }
                }
            }
        }
    }

    companion object {
        private const val KEY_ADD_TASK_ACTIVITY = "AddTaskActivity"

        fun getAddTaskScreenIntent(context: Context, time: Long): Intent {
            val launchIntent = Intent(context, AddTaskActivity::class.java)
            launchIntent.putExtra(KEY_ADD_TASK_ACTIVITY, time)
            return launchIntent
        }
    }
}