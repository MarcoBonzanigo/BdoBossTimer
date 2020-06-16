package com.scythetec.bdobosstimer.activities

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.view.Window.FEATURE_NO_TITLE
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import com.scythetec.bdobosstimer.R
import com.scythetec.bdobosstimer.function.getHtmlSpannedString
import com.scythetec.bdobosstimer.helper.BossHelper
import kotlinx.android.synthetic.main.activity_settings.*
import java.lang.reflect.Field


private lateinit var sharedPreferences: SharedPreferences
private var kzarka = BossHelper.SettingDuoState(com.scythetec.bdobosstimer.helper.kzarka)
private var karanda = BossHelper.SettingDuoState(com.scythetec.bdobosstimer.helper.karanda)
private var nouver = BossHelper.SettingDuoState(com.scythetec.bdobosstimer.helper.nouver)
private var kutum = BossHelper.SettingDuoState(com.scythetec.bdobosstimer.helper.kutum)
private var garmoth = BossHelper.SettingDuoState(com.scythetec.bdobosstimer.helper.garmoth)
private var offin = BossHelper.SettingDuoState(com.scythetec.bdobosstimer.helper.offin)
private var vell = BossHelper.SettingDuoState(com.scythetec.bdobosstimer.helper.vell)
private var quint = BossHelper.SettingDuoState(com.scythetec.bdobosstimer.helper.quint)
private var muraka = BossHelper.SettingDuoState(com.scythetec.bdobosstimer.helper.muraka)

private var monday = BossHelper.SettingTriState("monday" ,0)
private var tuesday = BossHelper.SettingTriState("tuesday" ,1)
private var wednesday = BossHelper.SettingTriState("wednesday" ,2)
private var thursday = BossHelper.SettingTriState("thursday" ,3)
private var friday = BossHelper.SettingTriState("friday" ,4)
private var saturday = BossHelper.SettingTriState("saturday",5 )
private var sunday = BossHelper.SettingTriState("sunday" ,6)

private var timeFrom = BossHelper.SettingDuplexState("timeFrom")
private var timeTo = BossHelper.SettingDuplexState("timeTo")

private var alertBefore = BossHelper.SettingFreeState("alertBefore")
private var alertTimes = BossHelper.SettingFreeState("alertTimes")
private var alertDelay = BossHelper.SettingFreeState("alertDelay")

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
        sharedPreferences = getSharedPreferences(id, Context.MODE_PRIVATE)
        settings_time_picker_from.setIs24HourView(true)
        settings_time_picker_to.setIs24HourView(true)

        loadSettings()

        settings_time_picker_from.hour = timeFrom.stateOne
        settings_time_picker_from.minute = timeFrom.stateTwo
        settings_number_alert_before.setText(alertBefore.state.toString())
        settings_number_alert_times.setText(alertTimes.state.toString())
        settings_number_alert_delay.setText(alertDelay.state.toString())

        settings_time_picker_to.hour = timeTo.stateOne
        settings_time_picker_to.minute = timeTo.stateTwo
        settings_time_picker_from.setOnTimeChangedListener { _, hourOfDay, minute ->
            timeFrom.set(sharedPreferences,hourOfDay,minute)
        }
        settings_time_picker_to.setOnTimeChangedListener { _, hourOfDay, minute ->
            timeTo.set(sharedPreferences,hourOfDay,minute)
        }
        settings_number_alert_before.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus){
                alertBefore.set(sharedPreferences,Integer.parseInt(settings_number_alert_before.text.toString()))
            }
        }
        settings_number_alert_times.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus){
                alertTimes.set(sharedPreferences,Integer.parseInt(settings_number_alert_times.text.toString()))
            }
        }
        settings_number_alert_delay.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus){
                alertDelay.set(sharedPreferences,Integer.parseInt(settings_number_alert_delay.text.toString()))
            }
        }

        settings_text_explain.text = resources.getHtmlSpannedString(R.string.explain)
    }

    private fun loadSettings() {
        toggleView(settings_image_boss_setting_kzarka,kzarka.update(sharedPreferences))
        toggleView(settings_image_boss_setting_karanda,karanda.update(sharedPreferences))
        toggleView(settings_image_boss_setting_nouver,nouver.update(sharedPreferences))
        toggleView(settings_image_boss_setting_kutum,kutum.update(sharedPreferences))
        toggleView(settings_image_boss_setting_garmoth,garmoth.update(sharedPreferences))
        toggleView(settings_image_boss_setting_offin,offin.update(sharedPreferences))
        toggleView(settings_image_boss_setting_vell,vell.update(sharedPreferences))
        toggleView(settings_image_boss_setting_quint,quint.update(sharedPreferences))
        toggleView(settings_image_boss_setting_muraka,muraka.update(sharedPreferences))
        toggleView(settings_button_monday, monday.update(sharedPreferences))
        toggleView(settings_button_tuesday, tuesday.update(sharedPreferences))
        toggleView(settings_button_wednesday, wednesday.update(sharedPreferences))
        toggleView(settings_button_thursday, thursday.update(sharedPreferences))
        toggleView(settings_button_friday, friday.update(sharedPreferences))
        toggleView(settings_button_saturday, saturday.update(sharedPreferences))
        toggleView(settings_button_sunday, sunday.update(sharedPreferences))
        timeFrom.update(sharedPreferences)
        timeTo.update(sharedPreferences)
        alertBefore.update(sharedPreferences)
        alertTimes.update(sharedPreferences)
        alertDelay.update(sharedPreferences)
    }

    fun onBossSettingChanged(view: View) {
        when (view.id) {
            R.id.settings_image_boss_setting_kzarka -> toggleView(settings_image_boss_setting_kzarka, kzarka.toggle(sharedPreferences))
            R.id.settings_image_boss_setting_karanda -> toggleView(settings_image_boss_setting_karanda, karanda.toggle(sharedPreferences))
            R.id.settings_image_boss_setting_nouver -> toggleView(settings_image_boss_setting_nouver, nouver.toggle(sharedPreferences))
            R.id.settings_image_boss_setting_kutum -> toggleView(settings_image_boss_setting_kutum, kutum.toggle(sharedPreferences))
            R.id.settings_image_boss_setting_offin -> toggleView(settings_image_boss_setting_offin, offin.toggle(sharedPreferences))
            R.id.settings_image_boss_setting_muraka -> toggleView(settings_image_boss_setting_muraka, muraka.toggle(sharedPreferences))
            R.id.settings_image_boss_setting_quint -> toggleView(settings_image_boss_setting_quint, quint.toggle(sharedPreferences))
            R.id.settings_image_boss_setting_garmoth -> toggleView(settings_image_boss_setting_garmoth, garmoth.toggle(sharedPreferences))
            R.id.settings_image_boss_setting_vell -> toggleView(settings_image_boss_setting_vell, vell.toggle(sharedPreferences))
        }
    }

    fun onDaySettingChanged(view: View) {
        when (view.id) {
            R.id.settings_button_monday -> toggleView(settings_button_monday, monday.toggle(sharedPreferences))
            R.id.settings_button_tuesday -> toggleView(settings_button_tuesday, tuesday.toggle(sharedPreferences))
            R.id.settings_button_wednesday -> toggleView(settings_button_wednesday, wednesday.toggle(sharedPreferences))
            R.id.settings_button_thursday -> toggleView(settings_button_thursday, thursday.toggle(sharedPreferences))
            R.id.settings_button_friday -> toggleView(settings_button_friday, friday.toggle(sharedPreferences))
            R.id.settings_button_saturday -> toggleView(settings_button_saturday, saturday.toggle(sharedPreferences))
            R.id.settings_button_sunday -> toggleView(settings_button_sunday, sunday.toggle(sharedPreferences))
        }
    }

    private fun toggleView(view: ImageView?, enabled: Boolean) {
        view?.setBackgroundResource(if (enabled) R.drawable.button_green else R.drawable.button_red)
    }

    private fun toggleView(view: Button?, state: Int) {
        view?.setBackgroundResource(if (state==1) R.drawable.button_green else if (state==2) R.drawable.button_orange else R.drawable.button_red)
    }

    fun setNumberPickerTextColor(numberPicker: NumberPicker, color: Int) {
        try {
            val selectorWheelPaintField: Field = numberPicker.javaClass
                .getDeclaredField("mSelectorWheelPaint")
            selectorWheelPaintField.isAccessible = true
            (selectorWheelPaintField.get(numberPicker) as Paint).color = color
        } catch (e: NoSuchFieldException) {
        } catch (e: IllegalAccessException) {
        } catch (e: IllegalArgumentException) {
        }
        val count = numberPicker.childCount
        for (i in 0 until count) {
            val child = numberPicker.getChildAt(i)
            if (child is EditText) child.setTextColor(color)
        }
        numberPicker.invalidate()
    }

    enum class ALERT_MODE{
        NORMAL, RESTRICTED, SILENT
    }


    class BossSettings(val alertTime: Int, val days: HashMap<Int,ALERT_MODE>, val silentFrom: Int, val silentTo: Int){

    }

}