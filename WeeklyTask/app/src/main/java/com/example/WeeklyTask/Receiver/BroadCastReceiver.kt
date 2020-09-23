package com.example.WeeklyTask.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import com.example.WeeklyTask.Realm.fncDone
import com.example.WeeklyTask.Realm.fncTask

class BroadCastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val calendar = Calendar.getInstance()
        var crntWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1

        //月曜日の場合だけ実行する
        if(crntWeek == 1) {
            val fDone = fncDone()
            val fTask = fncTask()
            fDone.insert()
            fDone.keepEightData()
            fTask.deleteNonFix()
            fTask.resetDoneFlg()
        }
    }
}