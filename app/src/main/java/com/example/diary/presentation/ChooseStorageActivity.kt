package com.example.diary.presentation

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.diary.DiaryApp
import com.example.diary.databinding.ActivityChooseStorageBinding
import com.example.diary.di.DaggerApplicationComponent

class ChooseStorageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseStorageBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityChooseStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE)

        if (!sharedPreferences.getString(REPOSITORY_TYPE_KEY, "").equals("")) {
            val intent = Intent(this, TaskListActivity::class.java)
            startActivity(intent)
            finish()
        }

        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val colorFilter = ColorMatrixColorFilter(colorMatrix)
        binding.jsonIV.colorFilter = colorFilter
        binding.roomIV.colorFilter = colorFilter

        var repository = ""

        binding.jsonIV.setOnClickListener {
            with(binding) {
                jsonIV.colorFilter = null
                jsonIV.alpha = 1.0f
                roomIV.colorFilter = colorFilter
                roomIV.alpha = 0.3f
            }
            repository = JSON_REPOSITORY_TYPE
            binding.checkBox.visibility = View.VISIBLE
            binding.continueButton.visibility = View.VISIBLE
        }

        binding.roomIV.setOnClickListener {
            with(binding) {
                roomIV.colorFilter = null
                roomIV.alpha = 1.0f
                jsonIV.colorFilter = colorFilter
                jsonIV.alpha = 0.3f
            }
            repository = ROOM_REPOSITORY_TYPE
            binding.checkBox.visibility = View.VISIBLE
            binding.continueButton.visibility = View.VISIBLE
        }



        binding.continueButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            chooseRepository(repository)
            sharedPreferences.edit().putBoolean(TASK_GENERATOR_KEY, binding.checkBox.isChecked).apply()
            val intent = Intent(this, TaskListActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun chooseRepository(repository: String) {
        sharedPreferences.edit()
            .putString(REPOSITORY_TYPE_KEY, repository)
            .apply()

        val app = application as DiaryApp
        app.component = DaggerApplicationComponent.factory().create(app, sharedPreferences)

    }

    companion object {
        private const val APP_SETTINGS = "settings"
        private const val REPOSITORY_TYPE_KEY = "repository_type"
        private const val TASK_GENERATOR_KEY = "task_generator"
        private const val JSON_REPOSITORY_TYPE = "Json"
        private const val ROOM_REPOSITORY_TYPE = "Room"
    }
}