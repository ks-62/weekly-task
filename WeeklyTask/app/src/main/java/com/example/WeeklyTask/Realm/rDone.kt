package com.example.WeeklyTask.Realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class rDone(
    @PrimaryKey
    var id: Int? = 0,
    var TOTAL_TASK: Int = 0,
    var CNT_DONE: Int = 0,
    var RATE_DONE: Int = 0
): RealmObject()