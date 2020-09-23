package com.example.WeeklyTask.Realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class rTask(
    @PrimaryKey
    var id: Int? = null,
    //月曜:1 〜 日曜:7, メモ:8
    var DATA_TYPE: Int = 0,
    //課題
    var TASK: String = "",
    //固定課題かどうか
    var FIX_FLG: Int = 0,
    //達成したかどうか
    var DONE_FLG: Int = 0
): RealmObject()