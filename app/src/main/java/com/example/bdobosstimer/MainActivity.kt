package com.example.bdobosstimer

import android.app.NotificationManager
import android.os.AsyncTask
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.bdobosstimer.MainActivity.BossRefresher
import kotlinx.android.synthetic.main.activity_main.*


private var refresher: BossRefresher? = null
class MainActivity : AppCompatActivity(), SynchronizedActivity{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onPause() {
        cancelRefresher()
        super.onPause()
    }

    override fun onDestroy() {
        cancelRefresher()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        synchronize()
    }

    private fun cancelRefresher() {
        refresher?.cancel(true)
        refresher = null
    }

    override fun synchronize() {
        updateBoss()
        refresher = BossRefresher(this)
        refresher!!.execute()
    }


    private fun updateBoss() {
        val nextBoss = BossHelper.instance.getNextBoss()
        main_text_boss_title.text = getString(
            R.string.nextBossAnnounce,
            if (nextBoss.name.contains("&")) "es" else "",
            if (nextBoss.name.contains("&")) "are" else "is",
            nextBoss.name,
            nextBoss.timeSpawn,
            TimeHelper().minutesToHoursAndMinutes(nextBoss.minutesToSpawn)
        )
        main_image_boss_one.setImageResource(nextBoss.bossOneImageResource!!)
        if (nextBoss.bossTwoImageResource != null) {
            main_image_boss_two.visibility = VISIBLE
            main_image_boss_two.setImageResource(nextBoss.bossTwoImageResource!!)
        } else {
            main_image_boss_two.visibility = GONE
        }
    }

    class BossRefresher(private val synchronizedActivity: SynchronizedActivity) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void?{
            Thread.sleep(10000)
            return null
        }

        override fun onPostExecute(result: Void?) {
            synchronizedActivity.synchronize()
        }
    }
}
