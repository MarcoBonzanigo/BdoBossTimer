package com.example.bdobosstimer

import android.content.Context
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
import com.example.bdobosstimer.MainActivity.BossRefresher
import kotlinx.android.synthetic.main.activity_main.*


private var refresher: BossRefresher? = null
private const val id = "bdo_boss_timer"
private lateinit var sharedPreferences: SharedPreferences
private const val nextBarterTime = "next_barter_time"
private const val parleyReduction = "parley_reduction"
private var bossOneMessage = ""
private var bossTwoMessage = ""

class MainActivity : AppCompatActivity(), SynchronizedActivity{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        supportActionBar?.hide(); //hide the title bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN); //show the activity in full screen
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences(id, Context.MODE_PRIVATE)
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
        val nextImperial = ImperialHelper.instance.getNextReset()
        //Imperial
        main_text_imperial_next.text = getString(R.string.next_imperial,TimeHelper.instance.minutesToHoursAndMinutes(nextImperial.timeDiffNext))
        main_text_imperial_prev.text = getString(R.string.prev_imperial,TimeHelper.instance.minutesToHoursAndMinutes(nextImperial.timeDiffPrev))
        //Bartering
        val nextBarterTimeAbsolute = sharedPreferences.getInt(nextBarterTime,0)
        val totalParleyReduction = (sharedPreferences.getInt(parleyReduction,0)*100/12).toInt()
        val nextBarterTimeTotal = nextBarterTimeAbsolute-totalParleyReduction
        val timeDifferenceToNow = TimeHelper.instance.getTimeDifferenceToNow(nextBarterTimeTotal)
        if (nextBarterTimeAbsolute == 0 || timeDifferenceToNow<0){
            main_text_barter_title.text = getString(R.string.reset_available)
            sharedPreferences.edit().putInt(nextBarterTime,0).apply()
            sharedPreferences.edit().putInt(parleyReduction,0).apply()
        }else{
            main_text_barter_title.text = getString(R.string.next_reset_in,TimeHelper.instance.minutesToHoursAndMinutes(timeDifferenceToNow), TimeHelper.instance.hundredToSixtyFormat(nextBarterTimeTotal))
        }
        //Previous Boss
        main_text_boss_title_previous.text = getString(
            R.string.previousBossAnnounce,TimeHelper.instance.minutesToHoursAndMinutes(previousBoss.minutesToSpawn*-1))
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

    fun onBarterButtonClick(view: View) {
        val  nextBarterTimeHundreds = sharedPreferences.getInt(nextBarterTime,0)
        if (nextBarterTimeHundreds == 0){
            val timeOfDay = TimeHelper.instance.getTimeOfTheDay()+400
            sharedPreferences.edit().putInt(nextBarterTime,timeOfDay).apply()
            sharedPreferences.edit().putInt(parleyReduction,0).apply()
            updateBoss()
        }else{
            barterResetCounter++
        }
        if (barterResetCounter==3){
            barterResetCounter = 0
            sharedPreferences.edit().putInt(nextBarterTime,0).apply()
            sharedPreferences.edit().putInt(parleyReduction,0).apply()
            main_text_barter_title.text = getString(R.string.reset_available)
            Toast.makeText(this, "Barter Timer has been reset!", Toast.LENGTH_SHORT).show()
        }
    }


    private var uniqueToast: Toast? = null

    fun cancelAndShowToast(message: String){
        uniqueToast?.cancel()
        uniqueToast = Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        )
        uniqueToast?.show()
    }


    fun onParleyReductionButtonClick(view: View) {
        changeParley(1)
    }

    fun onParleyIncreaseButtonClick(view: View) {
        changeParley(-1)
    }

    private fun changeParley(change: Int) {
        val  nextBarterTimeHundreds = sharedPreferences.getInt(nextBarterTime,0)
        if (nextBarterTimeHundreds == 0){
            cancelAndShowToast("Set Barter Timer first!")
            return
        }
        val parleyReductions = sharedPreferences.getInt(parleyReduction, 0) + change
        if (parleyReductions>=0) {
            sharedPreferences.edit().putInt(parleyReduction, parleyReductions).apply()
            cancelAndShowToast("Parley Reductions total: $parleyReductions (" +
                    NumberHelper.instance.formatThousands(parleyReductions) + ")")
            updateBoss()
        }
    }

    fun onBossClicked(view: View) {
        when (view.id) {
            R.id.main_image_boss_one -> {
                if (bossOneMessage.length > 0){
                    Toast.makeText(this, "Barter Timer has been reset!", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.main_image_boss_two -> {
                if (bossTwoMessage.length > 0){
                    Toast.makeText(this, "Barter Timer has been reset!", Toast.LENGTH_SHORT).show()
                }
            }
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
