package com.scythetec.bdobosstimer.activities

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startForegroundService
import com.scythetec.bdobosstimer.R
import com.scythetec.bdobosstimer.activities.MainActivity.BossRefresher
import com.scythetec.bdobosstimer.function.BossAlertService
import com.scythetec.bdobosstimer.function.Constants.Companion.updateMessage
import com.scythetec.bdobosstimer.function.getHtmlSpannedString
import com.scythetec.bdobosstimer.helper.BossHelper
import com.scythetec.bdobosstimer.helper.ImperialHelper
import com.scythetec.bdobosstimer.helper.NumberHelper
import com.scythetec.bdobosstimer.helper.TimeHelper
import kotlinx.android.synthetic.main.activity_main.*


const val id = "bdo_boss_timer"
private var refresher: BossRefresher? = null
private lateinit var sharedPreferences: SharedPreferences
private const val nextBarterTime = "next_barter_time"
private const val parleyReduction = "parley_reduction"
private var bossOneMessage = ""
private var bossTwoMessage = ""

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), SynchronizedActivity {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences(
            id, Context.MODE_PRIVATE
        )
        checkAndRunAlertService()
    }

    private fun communicateWithService() {
        val serviceIntent = Intent(applicationContext, BossAlertService::class.java)
        serviceIntent.putExtra(updateMessage,SettingsActivity.createSettingsObject(sharedPreferences))
        startService(serviceIntent)
    }

    private fun checkAndRunAlertService() {
        val intent = Intent(applicationContext, BossAlertService::class.java)
        startForegroundService(this,intent)
    }

    override fun onPause() {
        cancelRefresher()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        synchronize()
        communicateWithService()
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

    private fun resolveByTime(green: Int, yellow: Int, orange: Int, red: Int, time: Int): Int{
        return when {
            time <= 15 -> {
                red
            }
            time <= 30 -> {
                orange
            }
            time <= 60 -> {
                yellow
            }
            else -> {
                green
            }
        }
    }

    private fun updateBoss() {
        val nextBoss = BossHelper.instance.getNextBoss()
        val previousBoss = BossHelper.instance.getPreviousBoss()
        val nextImperial = ImperialHelper.instance.getNextReset()
        val imperialString = resolveByTime(
            R.string.next_imperial_green,
            R.string.next_imperial_yellow,
            R.string.next_imperial_orange,
            R.string.next_imperial_red,
            nextImperial.timeDiffNext
        )
        //Imperial
        main_text_imperial_next.text = resources.getHtmlSpannedString(imperialString,
            TimeHelper.instance.minutesToHoursAndMinutes(nextImperial.timeDiffNext))
        main_text_imperial_prev.text = getString(
            R.string.prev_imperial,
            TimeHelper.instance.minutesToHoursAndMinutes(nextImperial.timeDiffPrev))
        //Bartering
        val nextBarterTimeAbsolute = sharedPreferences.getInt(
            nextBarterTime,0)
        val totalParleyReduction = (sharedPreferences.getInt(
            parleyReduction,0)*100.0/12.0).toInt()
        val nextBarterTimeTotal = nextBarterTimeAbsolute-totalParleyReduction
        val barterTimeDifferenceToNow = TimeHelper.instance.getTimeDifferenceToNow(nextBarterTimeTotal)
        if (nextBarterTimeAbsolute == 0 || barterTimeDifferenceToNow<0 || barterTimeDifferenceToNow > 400){
            main_text_barter_title.text = getString(R.string.reset_available)
            sharedPreferences.edit().putInt(
                nextBarterTime,0).apply()
            sharedPreferences.edit().putInt(
                parleyReduction,0).apply()
        }else{
            val barterString = resolveByTime(
                R.string.next_reset_in_green,
                R.string.next_reset_in_yellow,
                R.string.next_reset_in_orange,
                R.string.next_reset_in_red,
                barterTimeDifferenceToNow
            )
            main_text_barter_title.text = resources.getHtmlSpannedString(barterString,
                TimeHelper.instance.minutesToHoursAndMinutes(barterTimeDifferenceToNow), TimeHelper.instance.hundredToSixtyFormat(nextBarterTimeTotal))
        }
        //Previous Boss
        main_text_boss_title_previous.text = getString(
            R.string.previous_boss_announce,
            TimeHelper.instance.minutesToHoursAndMinutes(previousBoss.minutesToSpawn*-1))
        main_image_boss_previous_one.setImageResource(previousBoss.bossOneImageResource!!)
        if (previousBoss.bossTwoImageResource != null) {
            main_image_boss_previous_two.visibility = VISIBLE
            main_image_boss_previous_two.setImageResource(previousBoss.bossTwoImageResource!!)
        } else {
            main_image_boss_previous_two.visibility = GONE
        }
        //Next Boss
        val bossString = resolveByTime(
            R.string.next_boss_announce_green,
            R.string.next_boss_announce_yellow,
            R.string.next_boss_announce_orange,
            R.string.next_boss_announce_red,
            nextBoss.minutesToSpawn
        )

        main_text_boss_title.text = resources.getHtmlSpannedString(
            bossString,
            if (nextBoss.name.contains("&")) "es" else "",
            if (nextBoss.name.contains("&")) "are" else "is",
            nextBoss.name.replace("&"," & "),
            nextBoss.timeSpawn,
            TimeHelper.instance.minutesToHoursAndMinutes(nextBoss.minutesToSpawn)
        )
        main_image_boss_one.setImageResource(nextBoss.bossOneImageResource!!)
        if (nextBoss.bossTwoImageResource != null) {
            main_image_boss_two.visibility = VISIBLE
            main_image_boss_two.setImageResource(nextBoss.bossTwoImageResource!!)
        } else {
            main_image_boss_two.visibility = GONE
        }
    }

    private var barterResetCounter = 0

    fun onBarterButtonClick(@Suppress("UNUSED_PARAMETER") view: View) {
        val  nextBarterTimeHundreds = sharedPreferences.getInt(
            nextBarterTime,0)
        if (nextBarterTimeHundreds == 0){
            val timeOfDay = TimeHelper.instance.getTimeOfTheDay()+400
            sharedPreferences.edit().putInt(
                nextBarterTime,timeOfDay).apply()
            sharedPreferences.edit().putInt(
                parleyReduction,0).apply()
            updateBoss()
        }else{
            barterResetCounter++
        }
        if (barterResetCounter==3){
            barterResetCounter = 0
            sharedPreferences.edit().putInt(
                nextBarterTime,0).apply()
            sharedPreferences.edit().putInt(
                parleyReduction,0).apply()
            main_text_barter_title.text = getString(R.string.reset_available)
            Toast.makeText(this, "Barter Timer has been reset!", Toast.LENGTH_SHORT).show()
        }
    }


    private var uniqueToast: Toast? = null

    private fun cancelAndShowToast(message: String){
        uniqueToast?.cancel()
        uniqueToast = Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        )
        uniqueToast?.show()
    }


    fun onParleyReductionButtonClick(@Suppress("UNUSED_PARAMETER") view: View) {
        changeParley(1)
    }

    fun onParleyIncreaseButtonClick(@Suppress("UNUSED_PARAMETER") view: View) {
        changeParley(-1)
    }

    private fun changeParley(change: Int) {
        val  nextBarterTimeHundreds = sharedPreferences.getInt(
            nextBarterTime,0)
        if (nextBarterTimeHundreds == 0){
            cancelAndShowToast("Set Barter Timer first!")
            return
        }
        val parleyReductions = sharedPreferences.getInt(
            parleyReduction, 0) + change
        if (parleyReductions>=0) {
            sharedPreferences.edit().putInt(
                parleyReduction, parleyReductions).apply()
            cancelAndShowToast("Parley Reductions total: $parleyReductions (" +
                    NumberHelper.instance.formatThousands(parleyReductions) + ")")
            updateBoss()
        }
    }

    fun onBossClicked(view: View) {
        when (view.id) {
            R.id.main_image_boss_one -> {
                if (bossOneMessage.isNotEmpty()){
                    Toast.makeText(this,
                        bossOneMessage, Toast.LENGTH_SHORT).show()
                }
            }
            R.id.main_image_boss_two -> {
                if (bossTwoMessage.isNotEmpty()){
                    Toast.makeText(this,
                        bossTwoMessage, Toast.LENGTH_SHORT).show()
                }
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

    fun executeTest(@Suppress("UNUSED_PARAMETER") view: View) {
        val intent = Intent(this, SettingsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
    }

}
