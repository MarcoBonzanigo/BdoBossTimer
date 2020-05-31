package com.example.bdobosstimer

import java.util.*
import kotlin.math.ceil

val timeGrid = arrayOf("00:15","02:00","05:00","09:00","12:00","16:00","19:00","22:15","23:15");
val timeIntGrid = arrayOf(25,200,500,900,1200,1600,1900,2225,2325)
const val kzarka = "Kzarka"
const val karanda = "Karanda"
const val nouver = "Nouver"
const val kutum = "Kutum"
const val garmoth = "Garmoth"
const val offin = "Offin"
const val vell = "Vell"
const val quint = "Quint"
const val muraka = "Muraka"
const val empty = ""
val bossGrid = arrayOf(
    arrayOf("$karanda&$kutum", karanda, kzarka, kzarka, offin, kutum, nouver, kzarka, empty),
    arrayOf(karanda, kutum, kzarka, nouver, kutum, nouver, karanda, garmoth, empty),
    arrayOf("$kutum&$kzarka", karanda, kzarka, karanda, empty, kutum, offin,"$karanda&$kzarka","$quint&$muraka"),
    arrayOf(nouver, kutum, nouver, kutum, nouver, kzarka, kutum, garmoth, empty),
    arrayOf("$kzarka&$karanda", nouver, karanda, kutum, karanda, nouver, kzarka,"$kutum&$kzarka",empty),
    arrayOf(karanda, offin, nouver, kutum, nouver,"$quint&$muraka","$karanda&$kzarka",empty,empty),
    arrayOf("$nouver&$kutum", kzarka, kutum, nouver, kzarka, vell, garmoth,"$kzarka&$nouver",empty)
)
val imageMap = hashMapOf<String,Int>(
    garmoth to R.drawable.garmoth_big,
    karanda to R.drawable.karanda_big,
    kutum to R.drawable.kutum_big,
    kzarka to R.drawable.kzarka_big,
    offin to R.drawable.offin_big,
    nouver to R.drawable.nouver_big,
    quint to R.drawable.quint_big,
    muraka to  R.drawable.muraka_big,
    vell to  R.drawable.vell_big
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
            if (time > now && bossGrid[getDayOfTheWeek()][pointer] != empty){
                return resolveBoss(now, pointer, getDayOfTheWeek())
            }
         }
         return resolveBoss(now-2400, 0, getDayOfTheWeek()+1)
     }


    fun getPreviousBoss(): Boss{
        val now = getTimeOfDay()
        for ((pointer, time) in timeIntGrid.withIndex()){
            if (time > now){
                return if ( pointer > 0) {
                    resolveBoss(now, pointer - 1, getDayOfTheWeek())
                }else{
                    var backPointer=1
                    while(bossGrid[getDayOfTheWeek()-1][timeIntGrid.size-backPointer] == empty){
                        backPointer++
                    }
                    resolveBoss(2400+now, timeIntGrid.size-backPointer, getDayOfTheWeek()-1)
                }
            }
        }
        return resolveBoss(2400+now, timeIntGrid.size-1, getDayOfTheWeek()-1)
    }


    private fun resolveBoss(now: Int, pointer: Int, timeOfWeek: Int): Boss {
        val time = timeIntGrid[pointer]
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

