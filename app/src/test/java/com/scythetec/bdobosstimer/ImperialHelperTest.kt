package com.scythetec.bdobosstimer

import com.scythetec.bdobosstimer.helper.ImperialHelper
import com.scythetec.bdobosstimer.helper.TimeHelper
import org.junit.Test

import org.junit.Assert.*

class ImperialHelperTest {
    @Test
    fun testNextReset() {
        TimeHelper.instance.setDebug(100,1)
        assertEquals("At 1:00 next Reset should be 2:00","02:00",
            ImperialHelper.instance.getNextReset().timeSpawn)
        TimeHelper.instance.setDebug(500,1)
        assertEquals("At 5:00 next Reset should be 8:00","08:00",
            ImperialHelper.instance.getNextReset().timeSpawn)
        TimeHelper.instance.setDebug(2350,1)
        assertEquals("At 23:30 next Reset should be 2:00","02:00",
            ImperialHelper.instance.getNextReset().timeSpawn)
        TimeHelper.instance.setNormal()
    }
}
