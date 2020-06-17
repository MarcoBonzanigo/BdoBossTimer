package com.scythetec.bdobosstimer.function

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import com.scythetec.bdobosstimer.R
import com.scythetec.bdobosstimer.function.Constants.Companion.updateMessage
import com.scythetec.bdobosstimer.helper.BossHelper
import com.scythetec.bdobosstimer.helper.BossSettings
import com.scythetec.bdobosstimer.helper.TimeHelper


class BossAlertService : Service() {
    private val binder = object : Binder(){
    }

    private var vibrator: Vibrator? = null
    private var mediaPlayer: MediaPlayer? = null
    private var bossAlertRefresher: BossAlertRefresher? = null
    private var bossSettings: BossSettings? = null

    override fun onCreate() {
        super.onCreate()
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        mediaPlayer = MediaPlayer.create(this, R.raw.inflicted)
        mediaPlayer?.isLooping = false
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        bossAlertRefresher?.cancel(true)
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val bossSettings: BossSettings? =  intent?.getParcelableExtra(updateMessage)
        if (bossSettings!= null){
            this.bossSettings = bossSettings
            bossAlertRefresher?.cancel(true)
        }
        bossAlertRefresher = BossAlertRefresher(mediaPlayer,vibrator,bossSettings)
        bossAlertRefresher?.soundsPlayed = 0
        bossAlertRefresher?.execute()
        return START_STICKY
    }

    class BossAlertRefresher(private val mediaPlayer: MediaPlayer?, private val vibrator: Vibrator?, private val bossSettings: BossSettings?) : AsyncTask<Void, Void, Void>() {

        var soundsPlayed = 0
        override fun doInBackground(vararg params: Void?): Void?{
            val limitMin = nvl(bossSettings?.alertBefore, 15)
            while (true){
                val nextBoss = BossHelper.instance.getNextBoss()
                if (checkAlertAllowed(nextBoss, bossSettings)){
                    mediaPlayer?.seekTo(0)
                    mediaPlayer?.start()
                    vibrate()
                    soundsPlayed++
                } else if (nextBoss.minutesToSpawn > limitMin ){
                    soundsPlayed = 0
                }
                Thread.sleep(1000L * nvl(bossSettings?.alertDelay,10))
            }
        }

        @Suppress("ConvertTwoComparisonsToRangeCheck")
        private fun checkAlertAllowed(nextBoss: BossHelper.Boss, bossSettings: BossSettings?): Boolean {
            val limitMin = nvl(bossSettings?.alertBefore, 15)
            //time to spawn
            if (nextBoss.minutesToSpawn > limitMin || soundsPlayed >= nvl(bossSettings?.alertTimes, 3)){
                return false
            }
            //check weekday
            var state = -1
            when (TimeHelper.instance.getDayOfTheWeek()){
                0 -> state = nvl(bossSettings?.monday,1)
                1 -> state = nvl(bossSettings?.tuesday,1)
                2 -> state = nvl(bossSettings?.wednesday,1)
                3 -> state = nvl(bossSettings?.thursday,1)
                5 -> state = nvl(bossSettings?.friday,1)
                4 -> state = nvl(bossSettings?.saturday,1)
                6 -> state = nvl(bossSettings?.sunday,1)
            }
            //disabled
            if (state == 3){
                return false
            }
            //check time
            if (state == 2){
                val timeFrom = nvl(bossSettings?.timeFrom, 0)
                val timeTo = nvl(bossSettings?.timeTo, 0)
                val timeOfTheDay = TimeHelper.instance.getTimeOfTheDay()
                if (timeFrom < timeTo){
                    //normal case
                    if (timeOfTheDay < timeFrom || timeOfTheDay > timeTo){
                        return false
                    }
                }else if (timeFrom > timeTo){
                    if (timeOfTheDay < timeFrom && timeOfTheDay > timeTo){
                        return false
                    }
                }
                //else, probably no time set, continue
            }
            //also continue for state 1
            //check boss
            val bosses = nextBoss.name.split("&")
            var enabled = false
            for (boss in bosses){
                if (nvl(bossSettings?.getEnabledBosses(), emptyList()).contains(boss)){
                    enabled = true
                }
            }
            return enabled
        }

        private fun vibrate() {
            if (nvl(bossSettings?.vibration,false)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator!!.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator!!.vibrate(500)
                }
            }
        }

        override fun onPostExecute(result: Void?) {
        }
    }
}