package com.scythetec.bdobosstimer.helper

import kotlin.properties.Delegates

class BossTestSettings(
    kzarka_: Boolean,
    karanda_: Boolean,
    nouver_: Boolean,
    kutum_: Boolean,
    garmoth_: Boolean,
    offin_: Boolean,
    vell_: Boolean,
    quint_: Boolean,
    muraka_: Boolean,
    monday_: Int,
    tuesday_: Int,
    wednesday_: Int,
    thursday_: Int,
    friday_: Int,
    saturday_: Int,
    sunday_: Int,
    timeFrom_: Int,
    timeTo_: Int,
    alertBefore_: Int,
    alertTimes_: Int,
    alertDelay_: Int,
    vibration_: Boolean
): IBossSettings {
    var kzarka by Delegates.notNull<Boolean>()
    var karanda by Delegates.notNull<Boolean>()
    var nouver by Delegates.notNull<Boolean>()
    var kutum by Delegates.notNull<Boolean>()
    var garmoth by Delegates.notNull<Boolean>()
    var offin by Delegates.notNull<Boolean>()
    var vell by Delegates.notNull<Boolean>()
    var quint by Delegates.notNull<Boolean>()
    var muraka by Delegates.notNull<Boolean>()
    var monday by Delegates.notNull<Int>()
    var tuesday by Delegates.notNull<Int>()
    var wednesday by Delegates.notNull<Int>()
    var thursday by Delegates.notNull<Int>()
    var friday by Delegates.notNull<Int>()
    var saturday by Delegates.notNull<Int>()
    var sunday by Delegates.notNull<Int>()
    var timeFrom by Delegates.notNull<Int>()
    var timeTo by Delegates.notNull<Int>()
    var alertBefore by Delegates.notNull<Int>()
    var alertTimes by Delegates.notNull<Int>()
    var alertDelay by Delegates.notNull<Int>()
    var vibration by Delegates.notNull<Boolean>()
    
    init {
        kzarka = kzarka_
        kzarka = kzarka_
        karanda = karanda_
        nouver = nouver_
        kutum = kutum_
        garmoth = garmoth_
        offin = offin_
        vell = vell_
        quint = quint_
        muraka = muraka_
        monday = monday_
        tuesday = tuesday_
        wednesday = wednesday_
        thursday = thursday_
        friday = friday_
        saturday = saturday_
        sunday = sunday_
        timeFrom = timeFrom_
        timeTo = timeTo_
        alertBefore = alertBefore_
        alertTimes = alertTimes_
        alertDelay = alertDelay_
        vibration	= vibration_
    }

    fun convertToBossSettings(): BossSettings? {
        return BossSettings(
            kzarka,
            karanda,
            nouver,
            kutum,
            garmoth,
            offin,
            vell,
            quint,
            muraka,
            monday,
            tuesday,
            wednesday,
            thursday,
            friday,
            saturday,
            sunday,
            timeFrom,
            timeTo,
            alertBefore,
            alertTimes,
            alertDelay,
            vibration
        )
    }
}