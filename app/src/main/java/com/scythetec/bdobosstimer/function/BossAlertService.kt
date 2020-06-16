package com.scythetec.bdobosstimer.function

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Binder
import android.os.IBinder
import com.scythetec.bdobosstimer.R
import com.scythetec.bdobosstimer.helper.BossHelper

class BossAlertService : Service() {
    private val binder = object : Binder(){
    }

    private var mediaPlayer: MediaPlayer? = null
    private var bossAlertRefresher: BossAlertRefresher? = null

    override fun onCreate() {
        super.onCreate()
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
        bossAlertRefresher = BossAlertRefresher(mediaPlayer)
        bossAlertRefresher?.execute()
        return START_STICKY
    }

    class BossAlertRefresher(private val mediaPlayer: MediaPlayer?) : AsyncTask<Void, Void, Void>() {

        var soundsPlayed = 0
        override fun doInBackground(vararg params: Void?): Void?{
            val limitMin = 124
            while (true){
                val nextBoss = BossHelper.instance.getNextBoss()
                if (nextBoss.minutesToSpawn <= limitMin && soundsPlayed < 3){
                    soundsPlayed++
                    mediaPlayer?.start()
                } else if (nextBoss.minutesToSpawn > limitMin ){
                    soundsPlayed = 0
                }
                Thread.sleep(10000)
            }
        }

        override fun onPostExecute(result: Void?) {
        }
    }
}