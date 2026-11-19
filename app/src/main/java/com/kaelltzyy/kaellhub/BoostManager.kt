package com.kaelltzyy.kaellhub

import android.app.ActivityManager
import android.content.Context

object BoostManager {
    fun boost(context: Context) {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        am.killBackgroundProcesses("com.android.chrome")
    }
    fun freeRamMb(context: Context): Int {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val info = ActivityManager.MemoryInfo()
        am.getMemoryInfo(info)
        return (info.availMem / 1_048_576).toInt()
    }
}
