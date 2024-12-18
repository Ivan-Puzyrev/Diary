package com.example.diary.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.diary.DiaryApp
import com.example.diary.R
import com.example.diary.databinding.ActivityTaskDetailsBinding
import javax.inject.Inject

class TaskDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityTaskDetailsBinding

    private val component by lazy {
        (application as DiaryApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val taskDetailsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TaskDetailsViewModel::class)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val taskId = intent.getIntExtra(KEY_TASK_ID, UNKNOWN_ID)
        taskDetailsViewModel.getTaskById(taskId)

        taskDetailsViewModel.task.observe(this, {
            binding.title.setText(it.name)
            val startHour = "${it.dateStart.hour.toString().padStart(2, '0')}:00"
            val finishHour = "${it.dateFinish.hour.toString().padStart(2, '0')}:00"
            binding.time.setText(getString(R.string.time, startHour, finishHour))
            binding.date.setText(
                getString(
                    R.string.date,
                    it.dateStart.month,
                    it.dateStart.dayOfMonth.toString(),
                    it.dateStart.year.toString()
                ))
            binding.description.setText(it.description)
        })

    }

    companion object {
        private const val KEY_TASK_ID = "task_id"
        private const val UNKNOWN_ID = -1

        fun getTaskDetailsIntent(
            context: Context,
            id: Int
        ): Intent {
            val launchIntent = Intent(context, TaskDetailsActivity::class.java).apply {
                putExtra(KEY_TASK_ID, id)
            }
            return launchIntent
        }
    }
}