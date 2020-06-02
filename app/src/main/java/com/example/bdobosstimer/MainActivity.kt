package com.example.bdobosstimer

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.example.bdobosstimer.MainActivity.BossRefresher
import kotlinx.android.synthetic.main.activity_main.*


private var refresher: BossRefresher? = null
private const val id = "bdo_boss_timer"
private lateinit var sharedPreferences: SharedPreferences
private const val nextBarterTime = "next_barter_time"
private const val testKey = "test_key"

class MainActivity : AppCompatActivity(), SynchronizedActivity{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences(id, Context.MODE_PRIVATE)

        //Hasi-Example for shared Preferences
        val testValueWrite = 1
        sharedPreferences.edit().putInt(testKey,testValueWrite).apply()
        val  testValueRead = sharedPreferences.getInt(testKey,0)
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
        val previousBoss = BossHelper.instance.getPreviousBoss()
        //Previous Boss
        main_text_boss_title_previous.text = getString(
            R.string.previousBossAnnounce,TimeHelper.minutesToHoursAndMinutes(previousBoss.minutesToSpawn*-1))
        main_image_boss_previous_one.setImageResource(previousBoss.bossOneImageResource!!)
        if (previousBoss.bossTwoImageResource != null) {
            main_image_boss_previous_two.visibility = VISIBLE
            main_image_boss_previous_two.setImageResource(previousBoss.bossTwoImageResource!!)
        } else {
            main_image_boss_previous_two.visibility = GONE
        }
        //Next Boss
        main_text_boss_title.text = getString(
            R.string.nextBossAnnounce,
            if (nextBoss.name.contains("&")) "es" else "",
            if (nextBoss.name.contains("&")) "are" else "is",
            nextBoss.name,
            nextBoss.timeSpawn,
            TimeHelper.minutesToHoursAndMinutes(nextBoss.minutesToSpawn)
        )
        main_image_boss_one.setImageResource(nextBoss.bossOneImageResource!!)
        if (nextBoss.bossTwoImageResource != null) {
            main_image_boss_two.visibility = VISIBLE
            main_image_boss_two.setImageResource(nextBoss.bossTwoImageResource!!)
        } else {
            main_image_boss_two.visibility = GONE
        }
    }


    fun bossSettingChanged(view: View) {
        when (view.id) {
            R.id.main_image_boss_setting_kzarka -> {

            }
            R.id.main_image_boss_setting_karanda -> {

            }
            R.id.main_image_boss_setting_nouver -> {

            }
            R.id.main_image_boss_setting_kutum -> {

            }
            R.id.main_image_boss_setting_offin -> {

            }
            R.id.main_image_boss_setting_muraka -> {

            }
            R.id.main_image_boss_setting_quint -> {

            }
            R.id.main_image_boss_setting_garmoth -> {

            }
            R.id.main_image_boss_setting_vell -> {

            }
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
