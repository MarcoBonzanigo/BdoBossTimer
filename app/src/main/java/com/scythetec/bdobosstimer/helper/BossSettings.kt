package com.scythetec.bdobosstimer.helper

import android.os.Parcel
import android.os.Parcelable
import com.scythetec.bdobosstimer.function.Constants

class BossSettings(
    val kzarka: Boolean,
    val karanda: Boolean,
    val nouver: Boolean,
    val kutum: Boolean,
    val garmoth: Boolean,
    val offin: Boolean,
    val vell: Boolean,
    val quint: Boolean,
    val muraka: Boolean,
    val monday: Int,
    val tuesday: Int,
    val wednesday: Int,
    val thursday: Int,
    val friday: Int,
    val saturday: Int,
    val sunday: Int,
    val timeFrom: Int,
    val timeTo: Int,
    val alertBefore: Int,
    val alertTimes: Int,
    val alertDelay: Int,
    val vibration: Boolean
): IBossSettings, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (kzarka) 1 else 0)
        parcel.writeByte(if (karanda) 1 else 0)
        parcel.writeByte(if (nouver) 1 else 0)
        parcel.writeByte(if (kutum) 1 else 0)
        parcel.writeByte(if (garmoth) 1 else 0)
        parcel.writeByte(if (offin) 1 else 0)
        parcel.writeByte(if (vell) 1 else 0)
        parcel.writeByte(if (quint) 1 else 0)
        parcel.writeByte(if (muraka) 1 else 0)
        parcel.writeInt(monday)
        parcel.writeInt(tuesday)
        parcel.writeInt(wednesday)
        parcel.writeInt(thursday)
        parcel.writeInt(friday)
        parcel.writeInt(saturday)
        parcel.writeInt(sunday)
        parcel.writeInt(timeFrom)
        parcel.writeInt(timeTo)
        parcel.writeInt(alertBefore)
        parcel.writeInt(alertTimes)
        parcel.writeInt(alertDelay)
        parcel.writeByte(if (vibration) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BossSettings> {
        override fun createFromParcel(parcel: Parcel): BossSettings {
            return BossSettings(parcel)
        }

        override fun newArray(size: Int): Array<BossSettings?> {
            return arrayOfNulls(size)
        }
    }

    fun getEnabledBosses(): List<String>{
        val bosses = ArrayList<String>()
        if (kzarka){
            bosses.add(Constants.kzarka)
        }
        if (karanda){
            bosses.add(Constants.karanda)
        }
        if (nouver){
            bosses.add(Constants.nouver)
        }
        if (kutum){
            bosses.add(Constants.kutum)
        }
        if (garmoth){
            bosses.add(Constants.garmoth)
        }
        if (offin){
            bosses.add(Constants.offin)
        }
        if (vell){
            bosses.add(Constants.vell)
        }
        if (quint){
            bosses.add(Constants.quint)
        }
        if (muraka){
            bosses.add(Constants.muraka)
        }
        return bosses
    }
}