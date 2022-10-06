package com.example.bitfit

import android.app.Application
import com.google.android.material.color.DynamicColors

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }

    val db by lazy { AppDatabase.getInstance(this) }
}