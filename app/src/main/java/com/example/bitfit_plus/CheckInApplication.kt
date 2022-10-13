package com.example.bitfit_plus

import android.app.Application

class CheckInApplication : Application() {
    val db by lazy { AppDatabase.getInstance(this) }
}