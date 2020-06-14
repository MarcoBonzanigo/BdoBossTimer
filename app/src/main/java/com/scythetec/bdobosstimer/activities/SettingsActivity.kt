package com.scythetec.bdobosstimer.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.Window.FEATURE_NO_TITLE
import android.view.WindowManager
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.scythetec.bdobosstimer.R
import com.scythetec.bdobosstimer.function.getHtmlSpannedString
import com.scythetec.bdobosstimer.helper.TimeHelper
import kotlinx.android.synthetic.main.activity_settings.*

private lateinit var sharedPreferences: SharedPreferences

@Suppress("ClassName")
class SettingsActivity:  AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(FEATURE_NO_TITLE)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_settings)
        sharedPreferences = getSharedPreferences(
            id, Context.MODE_PRIVATE)
        settings_time_picker_from.setIs24HourView(true)
        settings_time_picker_to.setIs24HourView(true)
        settings_time_picker_from.setOnTimeChangedListener { view, hourOfDay, minute ->

        }
        settings_time_picker_to.setOnTimeChangedListener { view, hourOfDay, minute ->

        }
        var text = "explain"
        settings_text_explain.text = resources.getHtmlSpannedString(R.string.explain)
        loadSettings()
    }

    private fun loadSettings() {
        TODO("Not yet implemented")
    }


    fun onBossSettingChanged(view: View) {
        when (view.id) {
            R.id.settings_image_boss_setting_kzarka -> {

            }
            R.id.settings_image_boss_setting_karanda -> {

            }
            R.id.settings_image_boss_setting_nouver -> {

            }
            R.id.settings_image_boss_setting_kutum -> {

            }
            R.id.settings_image_boss_setting_offin -> {

            }
            R.id.settings_image_boss_setting_muraka -> {

            }
            R.id.settings_image_boss_setting_quint -> {

            }
            R.id.settings_image_boss_setting_garmoth -> {

            }
            R.id.settings_image_boss_setting_vell -> {

            }
        }
    }


    fun onDaySettingChanged(view: View) {
        when (view.id) {
            R.id.settings_image_boss_setting_kzarka -> {

            }
            R.id.settings_image_boss_setting_karanda -> {

            }
            R.id.settings_image_boss_setting_nouver -> {

            }
            R.id.settings_image_boss_setting_kutum -> {

            }
            R.id.settings_image_boss_setting_offin -> {

            }
            R.id.settings_image_boss_setting_muraka -> {

            }
            R.id.settings_image_boss_setting_quint -> {

            }
            R.id.settings_image_boss_setting_garmoth -> {

            }
            R.id.settings_image_boss_setting_vell -> {

            }
        }
    }

    enum class ALERT_MODE{
        NORMAL, RESTRICTED, SILENT
    }


    class BossSettings(val alertTime: Int, val days: HashMap<Int,ALERT_MODE>, val silentFrom: Int, val silentTo: Int){

    }

}