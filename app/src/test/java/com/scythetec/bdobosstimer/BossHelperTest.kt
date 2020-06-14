package com.scythetec.bdobosstimer

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
}
