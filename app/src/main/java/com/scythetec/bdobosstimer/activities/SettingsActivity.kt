package com.scythetec.bdobosstimer.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.Window.FEATURE_NO_TITLE
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.scythetec.bdobosstimer.R
import com.scythetec.bdobosstimer.function.BossAlertService
import com.scythetec.bdobosstimer.function.Constants.Companion.alertBefore
import com.scythetec.bdobosstimer.function.Constants.Companion.alertDelay
import com.scythetec.bdobosstimer.function.Constants.Companion.alertTimes
import com.scythetec.bdobosstimer.function.Constants.Companion.friday
import com.scythetec.bdobosstimer.function.Constants.Companion.garmoth
import com.scythetec.bdobosstimer.function.Constants.Companion.karanda
import com.scythetec.bdobosstimer.function.Constants.Companion.kutum
import com.scythetec.bdobosstimer.function.Constants.Companion.kzarka
import com.scythetec.bdobosstimer.function.Constants.Companion.monday
import com.scythetec.bdobosstimer.function.Constants.Companion.muraka
import com.scythetec.bdobosstimer.function.Constants.Companion.nouver
import com.scythetec.bdobosstimer.function.Constants.Companion.offin
import com.scythetec.bdobosstimer.function.Constants.Companion.quint
import com.scythetec.bdobosstimer.function.Constants.Companion.saturday
import com.scythetec.bdobosstimer.function.Constants.Companion.sunday
import com.scythetec.bdobosstimer.function.Constants.Companion.thursday
import com.scythetec.bdobosstimer.function.Constants.Companion.timeFrom
import com.scythetec.bdobosstimer.function.Constants.Companion.timeTo
import com.scythetec.bdobosstimer.function.Constants.Companion.tuesday
import com.scythetec.bdobosstimer.function.Constants.Companion.updateMessage
import com.scythetec.bdobosstimer.function.Constants.Companion.vell
import com.scythetec.bdobosstimer.function.Constants.Companion.vibration
import com.scythetec.bdobosstimer.function.Constants.Companion.wednesday
import com.scythetec.bdobosstimer.function.getHtmlSpannedString
import com.scythetec.bdobosstimer.helper.BossHelper
import com.scythetec.bdobosstimer.helper.BossSettings
import com.scythetec.bdobosstimer.helper.BossTestSettings
import com.scythetec.bdobosstimer.helper.TimeHelper
import kotlinx.android.synthetic.main.activity_settings.*


private lateinit var sharedPreferences: SharedPreferences
private var kzarkaSettings = BossHelper.SettingDuoState(kzarka)
private var karandaSettings = BossHelper.SettingDuoState(karanda)
private var nouverSettings = BossHelper.SettingDuoState(nouver)
private var kutumSettings = BossHelper.SettingDuoState(kutum)
private var garmothSettings = BossHelper.SettingDuoState(garmoth)
private var offinSettings = BossHelper.SettingDuoState(offin)
private var vellSettings = BossHelper.SettingDuoState(vell)
private var quintSettings = BossHelper.SettingDuoState(quint)
private var murakaSettings = BossHelper.SettingDuoState(muraka)

private var mondaySettings = BossHelper.SettingTriState(monday,0)
private var tuesdaySettings = BossHelper.SettingTriState(tuesday,1)
private var wednesdaySettings = BossHelper.SettingTriState(wednesday,2)
private var thursdaySettings = BossHelper.SettingTriState(thursday,3)
private var fridaySettings = BossHelper.SettingTriState(friday,4)
private var saturdaySettings = BossHelper.SettingTriState(saturday,5 )
private var sundaySettings = BossHelper.SettingTriState(sunday,6)

private var timeFromSettings = BossHelper.SettingDuplexState(timeFrom)
private var timeToSettings = BossHelper.SettingDuplexState(timeTo)
private var alertBeforeSettings = BossHelper.SettingFreeState(alertBefore)
private var alertTimesSettings = BossHelper.SettingFreeState(alertTimes)
private var alertDelaySettings = BossHelper.SettingFreeState(alertDelay)
private var vibrationSettings = BossHelper.SettingDuoState(vibration)

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

        settings_time_picker_from.hour = timeFromSettings.stateOne
        settings_time_picker_from.minute = timeFromSettings.stateTwo
        settings_number_alert_before.setText(alertBeforeSettings.state.toString())
        settings_number_alert_times.setText(alertTimesSettings.state.toString())
        settings_number_alert_delay.setText(alertDelaySettings.state.toString())

        settings_time_picker_to.hour = timeToSettings.stateOne
        settings_time_picker_to.minute = timeToSettings.stateTwo
        settings_time_picker_from.setOnTimeChangedListener { _, hourOfDay, minute ->
            timeFromSettings.set(sharedPreferences,hourOfDay,minute)
        }
        settings_time_picker_to.setOnTimeChangedListener { _, hourOfDay, minute ->
            timeToSettings.set(sharedPreferences,hourOfDay,minute)
        }
        settings_number_alert_before.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus){
                alertBeforeSettings.set(sharedPreferences,Integer.parseInt(settings_number_alert_before.text.toString()))
            }
        }
        settings_number_alert_times.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus){
                alertTimesSettings.set(sharedPreferences,Integer.parseInt(settings_number_alert_times.text.toString()))
            }
        }
        settings_number_alert_delay.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus){
                alertDelaySettings.set(sharedPreferences,Integer.parseInt(settings_number_alert_delay.text.toString()))
            }
        }
        settings_toggle_alert_vibration.setOnCheckedChangeListener { _, _ ->
            vibrationSettings.toggle(sharedPreferences)
        }
        
        settings_text_explain.text = resources.getHtmlSpannedString(R.string.explain)
    }

    private fun loadSettings() {
        toggleView(settings_image_boss_setting_kzarka,kzarkaSettings.update(sharedPreferences))
        toggleView(settings_image_boss_setting_karanda,karandaSettings.update(sharedPreferences))
        toggleView(settings_image_boss_setting_nouver,nouverSettings.update(sharedPreferences))
        toggleView(settings_image_boss_setting_kutum,kutumSettings.update(sharedPreferences))
        toggleView(settings_image_boss_setting_garmoth,garmothSettings.update(sharedPreferences))
        toggleView(settings_image_boss_setting_offin,offinSettings.update(sharedPreferences))
        toggleView(settings_image_boss_setting_vell,vellSettings.update(sharedPreferences))
        toggleView(settings_image_boss_setting_quint,quintSettings.update(sharedPreferences))
        toggleView(settings_image_boss_setting_muraka,murakaSettings.update(sharedPreferences))
        toggleView(settings_button_monday, mondaySettings.update(sharedPreferences))
        toggleView(settings_button_tuesday, tuesdaySettings.update(sharedPreferences))
        toggleView(settings_button_wednesday, wednesdaySettings.update(sharedPreferences))
        toggleView(settings_button_thursday, thursdaySettings.update(sharedPreferences))
        toggleView(settings_button_friday, fridaySettings.update(sharedPreferences))
        toggleView(settings_button_saturday, saturdaySettings.update(sharedPreferences))
        toggleView(settings_button_sunday, sundaySettings.update(sharedPreferences))
        timeFromSettings.update(sharedPreferences)
        timeToSettings.update(sharedPreferences)
        alertBeforeSettings.update(sharedPreferences)
        alertTimesSettings.update(sharedPreferences)
        alertDelaySettings.update(sharedPreferences)
        settings_toggle_alert_vibration.isChecked = vibrationSettings.update(sharedPreferences)
    }

    fun onBossSettingChanged(view: View) {
        when (view.id) {
            R.id.settings_image_boss_setting_kzarka -> toggleView(settings_image_boss_setting_kzarka, kzarkaSettings.toggle(sharedPreferences))
            R.id.settings_image_boss_setting_karanda -> toggleView(settings_image_boss_setting_karanda, karandaSettings.toggle(sharedPreferences))
            R.id.settings_image_boss_setting_nouver -> toggleView(settings_image_boss_setting_nouver, nouverSettings.toggle(sharedPreferences))
            R.id.settings_image_boss_setting_kutum -> toggleView(settings_image_boss_setting_kutum, kutumSettings.toggle(sharedPreferences))
            R.id.settings_image_boss_setting_offin -> toggleView(settings_image_boss_setting_offin, offinSettings.toggle(sharedPreferences))
            R.id.settings_image_boss_setting_muraka -> toggleView(settings_image_boss_setting_muraka, murakaSettings.toggle(sharedPreferences))
            R.id.settings_image_boss_setting_quint -> toggleView(settings_image_boss_setting_quint, quintSettings.toggle(sharedPreferences))
            R.id.settings_image_boss_setting_garmoth -> toggleView(settings_image_boss_setting_garmoth, garmothSettings.toggle(sharedPreferences))
            R.id.settings_image_boss_setting_vell -> toggleView(settings_image_boss_setting_vell, vellSettings.toggle(sharedPreferences))
        }
        communicateWithService()
    }

    fun onDaySettingChanged(view: View) {
        when (view.id) {
            R.id.settings_button_monday -> toggleView(settings_button_monday, mondaySettings.toggle(sharedPreferences))
            R.id.settings_button_tuesday -> toggleView(settings_button_tuesday, tuesdaySettings.toggle(sharedPreferences))
            R.id.settings_button_wednesday -> toggleView(settings_button_wednesday, wednesdaySettings.toggle(sharedPreferences))
            R.id.settings_button_thursday -> toggleView(settings_button_thursday, thursdaySettings.toggle(sharedPreferences))
            R.id.settings_button_friday -> toggleView(settings_button_friday, fridaySettings.toggle(sharedPreferences))
            R.id.settings_button_saturday -> toggleView(settings_button_saturday, saturdaySettings.toggle(sharedPreferences))
            R.id.settings_button_sunday -> toggleView(settings_button_sunday, sundaySettings.toggle(sharedPreferences))
        }
    }

    private fun communicateWithService() {
        val serviceIntent = Intent(applicationContext, BossAlertService::class.java)
        serviceIntent.putExtra(updateMessage,createSettingsObject(sharedPreferences))
        startService(serviceIntent)
    }

    private fun toggleView(view: ImageView?, enabled: Boolean) {
        view?.setBackgroundResource(if (enabled) R.drawable.button_green else R.drawable.button_red)
    }

    private fun toggleView(view: Button?, state: Int) {
        view?.setBackgroundResource(if (state==1) R.drawable.button_green else if (state==2) R.drawable.button_orange else R.drawable.button_red)
    }

    companion object{
        fun createSettingsObject(sharedPreferences: SharedPreferences): BossSettings{
            return BossSettings(
                BossHelper.SettingDuoState(kzarka).updateSelf(sharedPreferences).get(),
                BossHelper.SettingDuoState(karanda).updateSelf(sharedPreferences).get(),
                BossHelper.SettingDuoState(nouver).updateSelf(sharedPreferences).get(),
                BossHelper.SettingDuoState(kutum).updateSelf(sharedPreferences).get(),
                BossHelper.SettingDuoState(garmoth).updateSelf(sharedPreferences).get(),
                BossHelper.SettingDuoState(offin).updateSelf(sharedPreferences).get(),
                BossHelper.SettingDuoState(vell).updateSelf(sharedPreferences).get(),
                BossHelper.SettingDuoState(quint).updateSelf(sharedPreferences).get(),
                BossHelper.SettingDuoState(muraka).updateSelf(sharedPreferences).get(),
                BossHelper.SettingTriState(monday,0).updateSelf(sharedPreferences).get(),
                BossHelper.SettingTriState(tuesday,1).updateSelf(sharedPreferences).get(),
                BossHelper.SettingTriState(wednesday,2).updateSelf(sharedPreferences).get(),
                BossHelper.SettingTriState(thursday,3).updateSelf(sharedPreferences).get(),
                BossHelper.SettingTriState(friday,4).updateSelf(sharedPreferences).get(),
                BossHelper.SettingTriState(saturday,5 ).updateSelf(sharedPreferences).get(),
                BossHelper.SettingTriState(sunday,6).updateSelf(sharedPreferences).get(),
                TimeHelper.instance.sixtyToHundredFormat(
                    BossHelper.SettingDuplexState(timeFrom).updateSelf(sharedPreferences).getOne(),
                    BossHelper.SettingDuplexState(timeFrom).updateSelf(sharedPreferences).getTwo()),
                TimeHelper.instance.sixtyToHundredFormat(
                    BossHelper.SettingDuplexState(timeTo).updateSelf(sharedPreferences).getOne(),
                    BossHelper.SettingDuplexState(timeTo).updateSelf(sharedPreferences).getTwo()),
                BossHelper.SettingFreeState(alertBefore).updateSelf(sharedPreferences).get(),
                BossHelper.SettingFreeState(alertTimes).updateSelf(sharedPreferences).get(),
                BossHelper.SettingFreeState(alertDelay).updateSelf(sharedPreferences).get(),
                BossHelper.SettingDuoState(vibration).updateSelf(sharedPreferences).get()
            )
        }
        fun createTestSettingsObject(): BossTestSettings {
            return BossTestSettings(
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                0,
                2399,
                15,
                5,
                5,
                false
            )
        }
    }
}