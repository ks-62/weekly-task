package com.example.WeeklyTask.Realm

import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where

class fncDone() {

    companion object {
        //Realmクラスのプロパティ作成
        val realm: Realm = Realm.getDefaultInstance()
    }

    // インスタンス取得
    fun getInstance() : Realm {
        return realm
    }

    /**
     * 達成率を追加
     */
    fun insert() {
        //DONE_FLGが1のデータの数を取得
        val countFf1 = realm.where(rTask::class.java).equalTo("DONE_FLG", 1.toInt()).findAll().size
        //メモ以外のデータの数を取得
        val totalData = realm.where(rTask::class.java).notEqualTo("DATA_TYPE", 8.toInt()).findAll().size
        //達成率の計算
        var donePercent = countFf1.toDouble() / totalData.toDouble()
        donePercent *= 100
        val insertDPer = donePercent.toInt()

        realm.executeTransaction {
            val maxId = realm.where(rDone::class.java).max("id")
            val nextId = (maxId?.toLong() ?: 0L) + 1
            val insertDone = realm.createObject<rDone>(nextId)

            //データ追加
            insertDone.TOTAL_TASK = totalData
            insertDone.CNT_DONE = countFf1
            insertDone.RATE_DONE = insertDPer

        }

    }

    /**
     * 達成率読み込み
     */
    fun read(): MutableList<Int> {
        val all = realm.where(rDone::class.java).findAll().sort("id")
        var doneList: MutableList<Int> = mutableListOf()
        all.forEach {
            doneList.add(it.RATE_DONE)
        }

        return doneList
    }

    /**
     * データの総数を8個に保つ
     */
    fun keepEightData() {
        realm.executeTransaction {
            var dRDone = realm.where(rDone::class.java)
            var cntRDone: Int = dRDone.count().toInt() //データの総数
            while(cntRDone > 8) {
                //データの総数が8個になるまで削除する
                val minId = dRDone.min("id")!!.toInt()
                val deRDone = dRDone.equalTo("id", minId).findAll()
                deRDone.deleteAllFromRealm()
                cntRDone = dRDone.count().toInt()
            }
        }

    }

    fun closeRealm() {
        realm.close()
    }



}