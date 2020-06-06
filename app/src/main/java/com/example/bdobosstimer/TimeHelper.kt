package com.example.bdobosstimer

import java.util.*
import kotlin.math.ceil
enum class MODE{
    NORMAL, DEBUG
}
private var mode = MODE.NORMAL
private var timeOfTheDay = 0
private var dayOfTheWeek = 0
class TimeHelper {

    companion object {
        val instance: TimeHelper = TimeHelper()
    }

    fun setDebug(timeOfTheDay0: Int, dayOfTheWeek0: Int){
        timeOfTheDay = timeOfTheDay0
        dayOfTheWeek = dayOfTheWeek0
        mode = MODE.DEBUG
    }

    fun setNormal(){
        timeOfTheDay = 0
        dayOfTheWeek = 0
        mode = MODE.NORMAL
    }

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
        if (mode==MODE.DEBUG){
            return dayOfTheWeek
        }
        val calendar = Calendar.getInstance()
        val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
        return normalizeDayOfTheWeek(weekDay,addDays)
    }

    fun getTimeOfTheDay(): Int {
        if (mode==MODE.DEBUG){
            return timeOfTheDay
        }
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minuteOfTheDay = calendar.get(Calendar.MINUTE)
        return hourOfDay*100+ ceil(minuteOfTheDay*1.6667).toInt()
    }

    fun getTimeDifference(time: Int, now: Int) = ((if (time>2400 && time-now>2400) (time%2400-now) else (time - now)) * 0.6).toInt()

    fun getTimeDifferenceToNow(time: Int) = getTimeDifference(time, getTimeOfTheDay())

    fun hundredToSixtyFormat(hundredTime: Int): String {
        val hours = hundredTime%2400/100
        val minutes = ((hundredTime-(hundredTime/100)*100)*0.6).toInt()
        val hoursStr = if (hours<10) "0$hours" else "$hours"
        val minutesStr = if (minutes<10) "0$minutes" else "$minutes"
        return "$hoursStr:$minutesStr"
    }

}