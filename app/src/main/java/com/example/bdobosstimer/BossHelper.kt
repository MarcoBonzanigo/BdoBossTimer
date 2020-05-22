package com.example.bdobosstimer

import java.util.*
import kotlin.math.ceil

val timeGrid = arrayOf("00:15","02:00","05:00","09:00","12:00","16:00","19:00","22:15","23:15");
val timeIntGrid = arrayOf(25,200,500,900,1200,1600,1900,2225,2325)
val bossGrid = arrayOf(
    arrayOf("Karanda&Kutum","Karanda","Kzarka","Kzarka","Offin","Kutum","Nouver","Kzarka",""),
    arrayOf("Karanda","Kutum","Kzarka","Nouver","Kutum","Nouver","Karanda","Garmoth",""),
    arrayOf("Kutum&Kzarka","Karanda","Kzarka","Karanda","","Kutum","Offin","Karanda&Kzarka","Quint&Muraka"),
    arrayOf("Nouver","Kutum","Nouver","Kutum","Nouver","Kzarka","Kutum","Garmoth",""),
    arrayOf("Kzarka&Karanda","Nouver","Karanda","Kutum","Karanda","Nouver","Kzarka","Kutum&Kzarka",""),
    arrayOf("Karanda","Offin","Nouver","Kutum","Nouver","Quint&Muaraka","Karanda&Kzarka","",""),
    arrayOf("Nouver&Kutum","Kzarka","Kutum","Nouver","Kzarka","Vell","Garmoth","Kzarka&Nouver","")
)
val imageMap = hashMapOf<String,Int>(
    "Garmoth" to R.drawable.garmoth_big,
    "Karanda" to R.drawable.karanda_big,
    "Kutum" to R.drawable.kutum_big,
    "Kzarka" to R.drawable.kzarka_big,
    "Offin" to R.drawable.offin_big,
    "Nouver" to R.drawable.nouver_big,
    "Quint" to R.drawable.quint_big,
    "Muraka" to  R.drawable.muraka_big,
    "Vell" to  R.drawable.vell_big
)

class BossHelper private constructor() {

    companion object {
        val instance: BossHelper = BossHelper()
    }


    fun normalizeDayOfTheWeek(weekDay: Int): Int {
        val alteredWeekDay = weekDay-2
        return if (alteredWeekDay < 0) alteredWeekDay + 7 else alteredWeekDay
    }

    private fun getDayOfTheWeek(): Int {
        val calendar = Calendar.getInstance()
        val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
        return normalizeDayOfTheWeek(weekDay)
    }

    private fun getTimeOfDay(): Int {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minuteOfTheDay = calendar.get(Calendar.MINUTE)
        return hourOfDay*100+ ceil(minuteOfTheDay*1.6667).toInt()
    }

     fun getNextBoss(): Boss{
         val now = getTimeOfDay()
         for ((pointer, time) in timeIntGrid.withIndex()){
            if (time > now && bossGrid[getDayOfTheWeek()][pointer] != ""){
                return resolveBoss(time, now, pointer, getDayOfTheWeek())
            }
         }
         return resolveBoss(timeIntGrid[0], now-2400, 0, getDayOfTheWeek()+1)
     }

    private fun resolveBoss(time: Int,now: Int, pointer: Int, timeOfWeek: Int): Boss {
        val timeDiff = ((time - now) * 0.6).toInt()
        val timeSpawn = timeGrid[pointer]
        val bossName = bossGrid[timeOfWeek][pointer]
        return Boss(bossName, timeSpawn, timeDiff)
    }

    class Boss(val name: String, val timeSpawn: String, val minutesToSpawn: Int){
        var bossOneImageResource: Int? = null
        var bossTwoImageResource: Int? = null

        init {
            if (name.contains("&")){
                val names = name.split("&")
                bossOneImageResource = imageMap[names[0]]
                bossTwoImageResource = imageMap[names[1]]
            }else{
                bossOneImageResource = imageMap[name]
            }
        }
    }
}

