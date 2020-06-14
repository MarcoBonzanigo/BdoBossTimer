package com.scythetec.bdobosstimer.function

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class StartupReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val myIntent = Intent(context, BossAlertService::class.java)
        context!!.startService(myIntent)
    }
}