package com.example.diary

import android.app.Application
import com.example.diary.di.ApplicationComponent
import com.example.diary.di.DaggerApplicationComponent

class DiaryApp : Application() {

    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        val sharedPreferences = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE)

        if (!sharedPreferences.contains(REPOSITORY_TYPE_KEY)) {
            sharedPreferences.edit()
                .putString(REPOSITORY_TYPE_KEY, "")
                .apply()
        }

        component = DaggerApplicationComponent.factory().create(this, sharedPreferences)
    }

    companion object {
        private const val APP_SETTINGS = "settings"
        private const val REPOSITORY_TYPE_KEY = "repository_type"
    }

}