package com.scythetec.bdobosstimer.function

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import com.scythetec.bdobosstimer.R
import com.scythetec.bdobosstimer.function.Constants.Companion.updateMessage
import com.scythetec.bdobosstimer.helper.BossHelper
import com.scythetec.bdobosstimer.helper.BossSettings


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
                if (BossHelper.instance.checkAlertAllowed(nextBoss, bossSettings, soundsPlayed)){
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