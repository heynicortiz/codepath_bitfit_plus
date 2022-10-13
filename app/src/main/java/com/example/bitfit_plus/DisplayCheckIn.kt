package com.example.bitfit_plus

import java.time.LocalDateTime

data class DisplayCheckIn(
    val timeOfDay: String?,
    val weight: Double?,
    val bodyFat: Double?,
    val bodyImage: String?,
    val date: LocalDateTime
) : java.io.Serializable