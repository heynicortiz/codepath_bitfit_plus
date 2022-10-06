package com.example.bitfit

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CheckInItemFetcher {
    companion object {
        val timeOfDay = listOf("Morning", "Evening", "Morning", "Evening")
        val weight = listOf(123.1, 127.2, 124.3, 126.4)
        val bodyFat = listOf(12.3, 12.7, 12.4, 12.6)
        val bodyImage = listOf("imagePath1", "imagePath2", "imagePath3", "imagePath4")
        val dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        val date = listOf(LocalDateTime.now().toString(), LocalDateTime.now().toString(), LocalDateTime.now().toString(), LocalDateTime.now().toString())

        fun getItems(): MutableList<CheckIn> {
            val checkInItems : MutableList<CheckIn> = ArrayList()
            for (i in timeOfDay.indices) {
                val item = CheckIn(timeOfDay[i], weight[i], bodyFat[i], bodyImage[i], date[i])
                checkInItems.add(item)
            }
            return checkInItems
        }
    }
}