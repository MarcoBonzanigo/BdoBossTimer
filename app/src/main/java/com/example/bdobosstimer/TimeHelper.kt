package com.example.bdobosstimer

class TimeHelper {
    fun minutesToHoursAndMinutes (minutes: Int): String {
        val hours = minutes/60
        val remainingMinutes = minutes - hours*60
        return if (hours == 0) "$remainingMinutes minutes" else "$hours hours and $remainingMinutes minutes"
    }
}