package com.scythetec.bdobosstimer

import com.scythetec.bdobosstimer.activities.SettingsActivity
import com.scythetec.bdobosstimer.helper.BossHelper
import com.scythetec.bdobosstimer.helper.TimeHelper
import org.junit.Test

import org.junit.Assert.*

class BossHelperTest {
    @Test
    fun testBossDataThroughoutWeek() {
        for (day in 0..6){
            for (time in 0..2359){
                TimeHelper.instance.setDebug(time,day)
                assertNotNull("Next boss should be not null for the combination of day($day) and time($time)",
                    BossHelper.instance.getNextBoss().bossOneImageResource)
                assertNotNull("Previous boss should be not null for the combination of day($day) and time($time)",
                    BossHelper.instance.getPreviousBoss().bossOneImageResource)
            }
        }
        TimeHelper.instance.setNormal()
    }

    @Test
    fun testBossSettings(){
        TimeHelper.instance.setDebug(1199,0)
        val disabledSettings = SettingsActivity.createTestSettingsObject()
        val nextBoss = BossHelper.instance.getNextBoss()
        var soundsPlayed = 0
        assertFalse(BossHelper.instance.checkAlertAllowed(nextBoss,disabledSettings,soundsPlayed))
        disabledSettings.monday = 1
        assertFalse(BossHelper.instance.checkAlertAllowed(nextBoss,disabledSettings,soundsPlayed))
        disabledSettings.offin = true
        assertTrue(BossHelper.instance.checkAlertAllowed(nextBoss,disabledSettings,soundsPlayed))
        soundsPlayed = 10
        assertFalse(BossHelper.instance.checkAlertAllowed(nextBoss,disabledSettings,soundsPlayed))
        soundsPlayed = 0
        disabledSettings.timeFrom = 1000
        disabledSettings.timeTo = 1300
        disabledSettings.monday = 2
        assertFalse(BossHelper.instance.checkAlertAllowed(nextBoss,disabledSettings,soundsPlayed))
    }
}
