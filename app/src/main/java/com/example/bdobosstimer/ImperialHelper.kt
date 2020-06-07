package com.example.bdobosstimer

private val timeGrid = arrayOf("02:00","05:00","08:00","11:00","14:00","17:00","20:00","23:00")
private val timeIntGrid = arrayOf(200,500,800,1100,1400,1700,2000,2300)

class ImperialHelper private constructor() {

    companion object {
        val instance: ImperialHelper = ImperialHelper()
    }

     fun getNextReset(): ImperialReset{
         val now = TimeHelper.instance.getTimeOfTheDay()
         for ((pointer, time) in timeIntGrid.withIndex()){
             if (time > now){
                return resolveReset(now, pointer)
            }
         }
         return resolveReset(now-2400, 0)
     }

    private fun resolveReset(now: Int, pointer: Int): ImperialReset {
        val time = timeIntGrid[pointer]
        val timeDiffNext = TimeHelper.instance.getTimeDifference(time, now)
        val timeDiffPrev = TimeHelper.instance.getTimeDifference(time, now+300)*-1
        val timeSpawn = timeGrid[pointer]
        return ImperialReset(timeSpawn, timeDiffNext, timeDiffPrev)
    }

    class ImperialReset(val timeSpawn: String, val timeDiffNext: Int, val timeDiffPrev: Int){
    }
}

