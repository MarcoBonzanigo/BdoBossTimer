package com.example.bdobosstimer

import org.junit.Test

import org.junit.Assert.*

class TimeHelperTest {
    @Test
    fun testWeekdayLogic() {
        assertEquals(0,TimeHelper.instance.normalizeDayOfTheWeek(2)) // Monday
        assertEquals(1,TimeHelper.instance.normalizeDayOfTheWeek(3)) // Tuesday
        assertEquals(2,TimeHelper.instance.normalizeDayOfTheWeek(4)) // Wednesday
        assertEquals(3,TimeHelper.instance.normalizeDayOfTheWeek(5)) // Thursday
        assertEquals(4,TimeHelper.instance.normalizeDayOfTheWeek(6)) // Friday
        assertEquals(5,TimeHelper.instance.normalizeDayOfTheWeek(7)) // Saturday
        assertEquals(6,TimeHelper.instance.normalizeDayOfTheWeek(1)) // Sunday

        assertEquals(0,TimeHelper.instance.normalizeDayOfTheWeek(1,1))
        assertEquals(1,TimeHelper.instance.normalizeDayOfTheWeek(1,2))
        assertEquals(2,TimeHelper.instance.normalizeDayOfTheWeek(1,3))
        assertEquals(3,TimeHelper.instance.normalizeDayOfTheWeek(1,4))
        assertEquals(4,TimeHelper.instance.normalizeDayOfTheWeek(1,5))
        assertEquals(5,TimeHelper.instance.normalizeDayOfTheWeek(1,6))
        assertEquals(6,TimeHelper.instance.normalizeDayOfTheWeek(1,7))

        assertEquals(0,TimeHelper.instance.normalizeDayOfTheWeek(1,1))
        assertEquals(1,TimeHelper.instance.normalizeDayOfTheWeek(2,1))
        assertEquals(2,TimeHelper.instance.normalizeDayOfTheWeek(3,1))
        assertEquals(3,TimeHelper.instance.normalizeDayOfTheWeek(4,1))
        assertEquals(4,TimeHelper.instance.normalizeDayOfTheWeek(5,1))
        assertEquals(5,TimeHelper.instance.normalizeDayOfTheWeek(6,1))
        assertEquals(6,TimeHelper.instance.normalizeDayOfTheWeek(7,1))
    }


     @Test
     fun testTimeDifference(){
         assertEquals(240, TimeHelper.instance.getTimeDifference(2500, 2100))
         assertEquals(180, TimeHelper.instance.getTimeDifference(2500, 2200))
         assertEquals(120, TimeHelper.instance.getTimeDifference(2500, 2300))
         assertEquals(60, TimeHelper.instance.getTimeDifference(2500, 2400))
         assertEquals(30, TimeHelper.instance.getTimeDifference(2500, 50))
     }

    @Test
    fun testHundredToSixtyConversion(){
        assertEquals("01:15", TimeHelper.instance.hundredToSixtyFormat(2525))
    }
}
