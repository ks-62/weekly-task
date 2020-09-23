package com.example.WeeklyTask.Realm

import android.widget.TextView
import com.example.weeklytask.R
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.createObject
import io.realm.kotlin.where

class fncTask() {

    companion object {
        //Realmクラスのプロパティ作成
        val realm: Realm = Realm.getDefaultInstance()
    }

    // インスタンス取得
    fun getInstance() : Realm {
        return realm
    }

    /**
     * rTask読み取り
     */
    fun read(type: Int): MutableList<String> {
        val all = realm.where(rTask::class.java).equalTo("DATA_TYPE", type).findAll().sort("id")
        var reList: MutableList<String> = mutableListOf()
        all.forEachIndexed { i, rTask ->
            reList.add(all[i]!!.TASK)
        }

        return reList
    }

    /**
     * 追加
     * 返り値
     *  0: データ重複
     *  1: データが10個以上存在
     *  2: インサート成功
     */
    fun insert(inputDataType: Int, inputTask: String, inputFixFlg: Int): Int {

            //同じ曜日に同じタスクかどうかチェック
            val checkDupData = realm.where(rTask::class.java)
                .equalTo("DATA_TYPE", inputDataType)
                .equalTo("TASK", inputTask)
                .findAll()

            if(checkDupData.isNotEmpty()){
                //データが重複していた場合は追加できない
                return 0

            } else {
                val countData = realm.where(rTask::class.java)
                    .equalTo("DATA_TYPE", inputDataType)
                    .findAll()
                    .size
                if(countData >= 10) {
                    //データが10個以上存在していた場合は追加できない
                    return 1
                } else {
                    realm.executeTransaction {
                        val maxId = realm.where(rTask::class.java).max("id")
                        val nextId = (maxId?.toLong() ?: 0L) + 1
                        val insertTask = realm.createObject<rTask>(nextId)

                        //データ追加
                        insertTask.DATA_TYPE = inputDataType
                        insertTask.TASK = inputTask
                        insertTask.FIX_FLG = inputFixFlg
                        insertTask.DONE_FLG = 0
                    }
                    return 2
                }
            }
    }

    /**
     * 更新
     */
    fun update(slctDataType: Int, slctTask: String, upDataType: Int, upTask: String, upFixFlg: Int): Boolean {
        //すでに存在しているデータと同じ内容かチェック
        val checkDup = realm.where(rTask::class.java)
            .equalTo("DATA_TYPE", upDataType)
            .equalTo("TASK", upTask)
            .findAll()
        if(checkDup.isNotEmpty()) {
            if(slctDataType == upDataType || slctTask == upTask) {
                //元のデータと同じ場合はfixflgのみ更新する
                realm.executeTransaction {
                    val upRTask = realm.where(rTask::class.java)
                        .equalTo("DATA_TYPE", slctDataType)
                        .equalTo("TASK", slctTask)
                        .findAll()
                    //データを追加
                    upRTask.setInt("DATA_TYPE", upDataType)
                    upRTask.setString("TASK", upTask)
                    upRTask.setInt("FIX_FLG", upFixFlg)
                }
                return true
            } else {
                return false
            }

        } else {
            //データの重複がない場合は更新する
            realm.executeTransaction {
                val upRTask = realm.where(rTask::class.java)
                    .equalTo("DATA_TYPE", slctDataType)
                    .equalTo("TASK", slctTask)
                    .findAll()
                upRTask.setInt("DATA_TYPE", upDataType)
                upRTask.setString("TASK", upTask)
                upRTask.setInt("FIX_FLG", upFixFlg)
            }
            return true
        }
    }

    /**
     * 削除
     */
    fun delete(deDataType: Int, deTask: String) {
        realm.executeTransaction {
            val deRTask = realm.where(rTask::class.java)
                .equalTo("DATA_TYPE", deDataType)
                .equalTo("TASK", deTask)
                .findAll()
            deRTask.deleteAllFromRealm()
        }
    }

    /**
     * idを取得
     */
    fun getId(day: Int): MutableList<Int?> {
        val all = realm.where(rTask::class.java).equalTo("DATA_TYPE", day).findAll().sort("id")
        val reId: MutableList<Int?> = mutableListOf()
        all.forEach {
            reId.add(it.id)
        }
        return reId
    }

    /**
     * 固定データ以外のデータを削除する
     */
    fun deleteNonFix() {
        val nonF: Int = 0
        realm.executeTransaction {
            var rTask = realm.where(rTask::class.java)
                .equalTo("FIX_FLG", nonF)
                .findAll()
            rTask.deleteAllFromRealm()
        }
    }

    /**
     * DONE_FLGをリセット
     */
    fun resetDoneFlg() {
        val rDf: Int = 1
        realm.executeTransaction {
            val rTask = realm.where(rTask::class.java).equalTo("DONE_FLG", rDf).findAll()
            rTask.forEach {
                rTask.setInt("DONE_FLG", 0)
            }
        }
    }

    /**
     * DONE_FLGが1のデータを取得
     */
    fun getDF1(argId: Int?): String {
        //DONE_FLGが1のデータを収納
        var df1: String = ""

        realm.executeTransaction {
            val rd = realm.where(rTask::class.java).equalTo("id", argId).findAll()
            if(rd[0]!!.DONE_FLG == 1) {
                df1 = rd[0]!!.TASK
            }
        }

        return df1
    }

    /**
     * FIX_FLGの取得
     */
    fun getFixFlg(sDataType: Int, sTask: String): Int {
        val fRTask = realm.where(rTask::class.java)
            .equalTo("DATA_TYPE", sDataType)
            .equalTo("TASK", sTask)
            .findAll()

        return fRTask[0]!!.FIX_FLG
    }

    /**
     * DONE_FLGを0に変更
     */
    fun turnDF0(textTask: String, intDt: Int) {
        realm.executeTransaction {
            val targetF = realm.where(rTask::class.java)
                .equalTo("DATA_TYPE", intDt)
                .equalTo("TASK", textTask)
                .findAll()
            targetF.setInt("DONE_FLG", 0)
        }
    }

    /**
     * DONE_FLGを1に変更
     */
    fun turnDF1(textTask: String, intDt: Int) {
        realm.executeTransaction {
            val targetF = realm.where(rTask::class.java)
                .equalTo("DATA_TYPE", intDt)
                .equalTo("TASK", textTask)
                .findAll()
            targetF.setInt("DONE_FLG", 1)
        }
    }

    fun closeRealm() {
        realm.close()
    }


}