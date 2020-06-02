package com.example.bdobosstimer

import java.util.*
import kotlin.math.ceil

class TimeHelper {

    companion object{
        fun minutesToHoursAndMinutes (minutes: Int): String {
            val hours = minutes/60
            val remainingMinutes = minutes - hours*60
            return if (hours == 0) "$remainingMinutes minutes" else "$hours hours and $remainingMinutes minutes"
        }

        fun normalizeDayOfTheWeek(weekDay: Int, addDays: Int = 0): Int {
            val alteredWeekDay = (weekDay+addDays)%7-2
            return if (alteredWeekDay < 0) alteredWeekDay + 7 else alteredWeekDay
        }

        fun getDayOfTheWeek(addDays: Int = 0): Int {
            val calendar = Calendar.getInstance()
            val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
            return normalizeDayOfTheWeek(weekDay,addDays)
        }

        fun getTimeOfDay(): Int {
            val calendar = Calendar.getInstance()
            val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
            val minuteOfTheDay = calendar.get(Calendar.MINUTE)
            return hourOfDay*100+ ceil(minuteOfTheDay*1.6667).toInt()
        }

        fun getTimeDifference(time: Int, now: Int) = ((if (time>2400 && time-now>2400) (time%2400-now) else (time - now)) * 0.6).toInt()

        fun getTimeDifferenceToNow(time: Int) = getTimeDifference(time, getTimeOfDay())
    }

}