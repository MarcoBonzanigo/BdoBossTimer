package com.example.bdobosstimer

import org.junit.Test

import org.junit.Assert.*

class BossHelperTest {
    @Test
    fun testWeekdayLogic() {
        assertEquals(0,BossHelper.instance.normalizeDayOfTheWeek(2)) // Monday
        assertEquals(1,BossHelper.instance.normalizeDayOfTheWeek(3)) // Tuesday
        assertEquals(2,BossHelper.instance.normalizeDayOfTheWeek(4)) // Wednesday
        assertEquals(3,BossHelper.instance.normalizeDayOfTheWeek(5)) // Thursday
        assertEquals(4,BossHelper.instance.normalizeDayOfTheWeek(6)) // Friday
        assertEquals(5,BossHelper.instance.normalizeDayOfTheWeek(7)) // Saturday
        assertEquals(6,BossHelper.instance.normalizeDayOfTheWeek(1)) // Sunday

        assertEquals(0,BossHelper.instance.normalizeDayOfTheWeek(1,1))
        assertEquals(1,BossHelper.instance.normalizeDayOfTheWeek(1,2))
        assertEquals(2,BossHelper.instance.normalizeDayOfTheWeek(1,3))
        assertEquals(3,BossHelper.instance.normalizeDayOfTheWeek(1,4))
        assertEquals(4,BossHelper.instance.normalizeDayOfTheWeek(1,5))
        assertEquals(5,BossHelper.instance.normalizeDayOfTheWeek(1,6))
        assertEquals(6,BossHelper.instance.normalizeDayOfTheWeek(1,7))

        assertEquals(0,BossHelper.instance.normalizeDayOfTheWeek(1,1))
        assertEquals(1,BossHelper.instance.normalizeDayOfTheWeek(2,1))
        assertEquals(2,BossHelper.instance.normalizeDayOfTheWeek(3,1))
        assertEquals(3,BossHelper.instance.normalizeDayOfTheWeek(4,1))
        assertEquals(4,BossHelper.instance.normalizeDayOfTheWeek(5,1))
        assertEquals(5,BossHelper.instance.normalizeDayOfTheWeek(6,1))
        assertEquals(6,BossHelper.instance.normalizeDayOfTheWeek(7,1))
    }
}
