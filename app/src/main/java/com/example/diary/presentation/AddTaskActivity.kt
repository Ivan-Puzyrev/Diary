package com.example.diary.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.NumberPicker
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

    lateinit var binding: ActivityAddTaskBinding
    lateinit var taskList: List<Task>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val addTaskViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AddTaskViewModel::class)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addTaskViewModel.taskListLD.observe(this, {
            taskList = it
        })
        val date = intent.getLongExtra(KEY_ADD_TASK_ACTIVITY, 0)

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
        binding.numberPickerStart.apply {
            minValue = 0
            maxValue = itemsTimeStart.size - 1
            displayedValues = itemsTimeStart
            value = 12
        }
        binding.numberPickerEnd.apply {
            minValue = 0
            maxValue = itemsTimeEnd.size - 1
            displayedValues = itemsTimeEnd
            value = 12
        }

        binding.numberPickerStart.setOnValueChangedListener(object :
            NumberPicker.OnValueChangeListener {
            override fun onValueChange(picker: NumberPicker?, oldValue: Int, newValue: Int) {
                if (newValue == 0 && binding.numberPickerEnd.value == 23) {
                    binding.numberPickerEnd.value = 0
                } else {
                    if (newValue >= binding.numberPickerEnd.value) {
                        binding.numberPickerEnd.value = newValue
                    }
                }
            }
        })

        binding.numberPickerEnd.setOnValueChangedListener(object :
            NumberPicker.OnValueChangeListener {
            override fun onValueChange(picker: NumberPicker?, oldValue: Int, newValue: Int) {
                if (newValue == 23 && binding.numberPickerStart.value == 0) {
                    binding.numberPickerStart.value = 23
                } else {
                    if (newValue <= binding.numberPickerStart.value) {
                        binding.numberPickerStart.value = newValue
                    }
                }
            }
        })


        binding.saveButton.setOnClickListener {
            if (binding.nameEditText.text.toString().trim().isEmpty()) {
                Toast.makeText(this, getString(R.string.fill_in_the_name_field), Toast.LENGTH_SHORT)
                    .show()
            } else {
                val dateStart =
                    Instant.ofEpochSecond(date + binding.numberPickerStart.value * 3600.toLong())
                        .atZone(ZoneId.systemDefault())
                val dateEnd =
                    Instant.ofEpochSecond(date + (binding.numberPickerEnd.value + 1) * 3600.toLong())
                        .atZone(ZoneId.systemDefault())
                var id = 0
                if (!(taskList == null || taskList.isEmpty())) {
                    id = taskList.size
                }
                val task = Task(
                    id,
                    dateStart,
                    dateEnd,
                    binding.nameEditText.text.toString().trim(),
                    binding.descriptionEditText.text.toString().trim()
                )
                addTaskViewModel.addTask(task)
                finish()
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