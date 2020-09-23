package com.example.WeeklyTask

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class weeklyTaskApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        //Realmの初期化
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
    }
}