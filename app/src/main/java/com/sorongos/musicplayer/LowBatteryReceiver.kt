package com.sorongos.musicplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class LowBatteryReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("onReceive","${intent.action}")
        when(intent.action){
            Intent.ACTION_BATTERY_LOW ->{
                Log.d("onReceive","battery low")
            }
        }
    }
}