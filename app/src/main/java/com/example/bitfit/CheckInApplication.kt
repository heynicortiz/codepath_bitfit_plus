package com.example.bitfit

import android.app.Application

class CheckInApplication : Application() {
    val db by lazy { AppDatabase.getInstance(this) }
}