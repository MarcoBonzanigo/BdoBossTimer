package com.scythetec.bdobosstimer.helper

import android.content.SharedPreferences
import com.scythetec.bdobosstimer.R
import com.scythetec.bdobosstimer.function.Constants.Companion.empty
import com.scythetec.bdobosstimer.function.Constants.Companion.garmoth
import com.scythetec.bdobosstimer.function.Constants.Companion.karanda
import com.scythetec.bdobosstimer.function.Constants.Companion.kutum
import com.scythetec.bdobosstimer.function.Constants.Companion.kzarka
import com.scythetec.bdobosstimer.function.Constants.Companion.muraka
import com.scythetec.bdobosstimer.function.Constants.Companion.nouver
import com.scythetec.bdobosstimer.function.Constants.Companion.offin
import com.scythetec.bdobosstimer.function.Constants.Companion.quint
import com.scythetec.bdobosstimer.function.Constants.Companion.vell

private val timeGrid = arrayOf("00:15","02:00","05:00","09:00","12:00","16:00","19:00","22:15","23:15")
private val timeIntGrid = arrayOf(25,200,500,900,1200,1600,1900,2225,2325)
private val bossGrid = arrayOf(
    arrayOf("$karanda&$kutum",
        karanda,
        kzarka,
        kzarka,
        offin,
        kutum,
        nouver,
        kzarka,
        empty
    ),
    arrayOf(
        karanda,
        kutum,
        kzarka,
        nouver,
        kutum,
        nouver,
        karanda,
        garmoth,
        empty
    ),
    arrayOf("$kutum&$kzarka",
        karanda,
        kzarka,
        karanda,
        empty,
        kutum,
        offin,"$karanda&$kzarka","$quint&$muraka"),
    arrayOf(
        nouver,
        kutum,
        nouver,
        kutum,
        nouver,
        kzarka,
        kutum,
        garmoth,
        empty
    ),
    arrayOf("$kzarka&$karanda",
        nouver,
        karanda,
        kutum,
        karanda,
        nouver,
        kzarka,"$kutum&$kzarka",
        empty
    ),
    arrayOf(
        karanda,
        offin,
        nouver,
        kutum,
        nouver,"$quint&$muraka","$karanda&$kzarka",
        empty,
        empty
    ),
    arrayOf("$nouver&$kutum",
        kzarka,
        kutum,
        nouver,
        kzarka,
        vell,
        garmoth,"$kzarka&$nouver",
        empty
    )
)
private val imageMap = hashMapOf(
    garmoth to R.drawable.garmoth_big,
    karanda to R.drawable.karanda_big,
    kutum to R.drawable.kutum_big,
    kzarka to R.drawable.kzarka_big,
    offin to R.drawable.offin_big,
    nouver to R.drawable.nouver_big,
    quint to R.drawable.quint_big,
    muraka to R.drawable.muraka_big,
    vell to R.drawable.vell_big
)

class BossHelper private constructor() {

    companion object {
        val instance: BossHelper =
            BossHelper()
    }

     fun getNextBoss(): Boss {
         val now = TimeHelper.instance.getTimeOfTheDay()
         val dayOfTheWeek = TimeHelper.instance.getDayOfTheWeek()
         for ((pointer, time) in timeIntGrid.withIndex()){
             if (time > now && bossGrid[dayOfTheWeek][pointer] != empty){
                return resolveBoss(now, pointer, dayOfTheWeek)
            }
         }
         return resolveBoss(now-2400, 0, TimeHelper.instance.getDayOfTheWeek(1))
     }


    fun getPreviousBoss(): Boss {
        val now = TimeHelper.instance.getTimeOfTheDay()
        val dayOfTheWeek = TimeHelper.instance.getDayOfTheWeek()
        for ((pointer, time) in timeIntGrid.withIndex()){
            if (time > now || pointer+1 == timeIntGrid.size){
                return if ( pointer > 0 && bossGrid[dayOfTheWeek][pointer-1] == empty) {
                    resolveBoss(now, pointer - 2, dayOfTheWeek)
                }else if (pointer > 0){
                    resolveBoss(now, pointer - 1, dayOfTheWeek)
                }else{
                    var backPointer=1
                    while(bossGrid[TimeHelper.instance.getDayOfTheWeek(-1)][timeIntGrid.size-backPointer] == empty){
                        backPointer++
                    }
                    resolveBoss(2400+now, timeIntGrid.size-backPointer, TimeHelper.instance.getDayOfTheWeek(-1))
                }
            }
        }
        return resolveBoss(2400+now, timeIntGrid.size-1, dayOfTheWeek)
    }


    private fun resolveBoss(now: Int, pointer: Int, dayOfWeek: Int): Boss {
        val time = timeIntGrid[pointer]
        val timeDiff = TimeHelper.instance.getTimeDifference(time, now)
        val timeSpawn = timeGrid[pointer]
        val bossName = bossGrid[dayOfWeek][pointer]
        return Boss(
            bossName,
            timeSpawn,
            timeDiff
        )
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

    class SettingDuoState(private val name: String,private var enabled: Boolean = true){
        fun toggle(sharedPreferences: SharedPreferences): Boolean {
            enabled = !enabled
            sharedPreferences.edit().putBoolean(name,enabled).apply()
            return enabled
        }

        fun update(sharedPreferences: SharedPreferences): Boolean {
            enabled = sharedPreferences.getBoolean(name,true)
            return enabled
        }

        fun updateSelf(sharedPreferences: SharedPreferences): SettingDuoState{
            update(sharedPreferences)
            return this
        }

        fun get(): Boolean{
            return enabled
        }
    }

    class SettingTriState(private val name: String, private var id: Int = -1, private var state: Int = 1){
        fun toggle(sharedPreferences: SharedPreferences): Int {
            state = (state+1)%3
            sharedPreferences.edit().putInt(name,state).apply()
            if (id >= 0){
                sharedPreferences.edit().putInt(name+"Id",id).apply()
            }
            return state
        }

        fun update(sharedPreferences: SharedPreferences): Int {
            state = sharedPreferences.getInt(name,1)
            id = sharedPreferences.getInt(name+"Id",-1)
            return state
        }

        fun updateSelf(sharedPreferences: SharedPreferences): SettingTriState{
            update(sharedPreferences)
            return this
        }

        fun get(): Int{
            return state
        }
    }

    class SettingDuplexState(private val name: String, var stateOne: Int = 1, var stateTwo: Int = 1){
        fun set(sharedPreferences: SharedPreferences, stateOne: Int, stateTwo: Int) {
            this.stateOne = stateOne
            this.stateTwo = stateTwo
            sharedPreferences.edit().putInt(name+"1",stateOne).apply()
            sharedPreferences.edit().putInt(name+"2",stateTwo).apply()
        }

        fun update(sharedPreferences: SharedPreferences) {
            stateOne = sharedPreferences.getInt(name+"1",1)
            stateTwo = sharedPreferences.getInt(name+"2",1)
        }

        fun updateSelf(sharedPreferences: SharedPreferences): SettingDuplexState{
            update(sharedPreferences)
            return this
        }

        fun getOne(): Int{
            return stateOne
        }

        fun getTwo(): Int{
            return stateTwo
        }
    }

    class SettingFreeState(private val name: String, var state: Int = 0){
        fun set(sharedPreferences: SharedPreferences, state: Int) {
            this.state = state
            sharedPreferences.edit().putInt(name,state).apply()
        }

        fun update(sharedPreferences: SharedPreferences) {
            state = sharedPreferences.getInt(name,0)
        }

        fun updateSelf(sharedPreferences: SharedPreferences): SettingFreeState{
            update(sharedPreferences)
            return this
        }

        fun get(): Int{
            return state
        }
    }
}

