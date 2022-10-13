package com.example.bitfit_plus

data class CheckIn(
    val timeOfDay: String?,
    val weight: Double?,
    val bodyFat: Double?,
    val bodyImage: String?,
    val date: String?
) : java.io.Serializable