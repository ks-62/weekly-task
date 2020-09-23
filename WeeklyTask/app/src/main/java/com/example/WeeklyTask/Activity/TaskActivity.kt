package com.example.WeeklyTask.Activity

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.example.WeeklyTask.Adapter.cAdapter
import com.example.WeeklyTask.Fragment.AddFragment
import com.example.WeeklyTask.Fragment.DeleteFragment
import com.example.WeeklyTask.Function.HeightAnimation
import com.example.WeeklyTask.Function.Utility
import com.example.WeeklyTask.Function.changeUnit
import com.example.WeeklyTask.Realm.fncDone
import com.example.WeeklyTask.Realm.fncTask
import com.example.WeeklyTask.Receiver.BroadCastReceiver
import com.example.weeklytask.R
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.listview_with_button.view.*

class TaskActivity : AppCompatActivity() {

    //Realmクラスのプロパティ作成
    private lateinit var realmTask: Realm
    private lateinit var realmDone: Realm

    //レイアウト完成時に処理をする
    inline fun View.afterMeasured(crossinline f: View.() -> Unit) {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (measuredHeight > 0 && measuredWidth > 0) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    f()
                }
            }
        })
    }

    /**
     * 高さパラメータ月曜日
     */
    var moTH = 0
    var moLH = 0

    /**
     * 高さパラメータ火曜日
     */
    var tuTH = 0
    var tuLH = 0

    /**
     * 高さパラメータ水曜日
     */
    var weTH = 0
    var weLH = 0

    /**
     * 高さパラメータ木曜日
     */
    var thTH = 0
    var thLH = 0

    /**
     * 高さパラメータ金曜日
     */
    var frTH = 0
    var frLH = 0

    /**
     * 高さパラメータ土曜日
     */
    var saTH = 0
    var saLH = 0

    /**
     * 高さパラメータ日曜日
     */
    var suTH = 0
    var suLH = 0

    /**
     * 高さパラメータメモ
     */
    var meTH = 0
    var meLH = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        try {
            //Realmクラスのインスタンス作成
        realmTask = fncTask().getInstance()
        realmDone = fncDone().getInstance()

            //初期化
            init()

            //リスナー
            setListener()

            //月曜日に実行
            calcAchieve()
        }
        catch (e: Exception) {
            AlertDialog.Builder(this)
                .setTitle("エラー")
                .setMessage("エラーが発生しました。")
                .show()
        }


    }

    /**
     * 初期化処理
     */
    fun init() {

        //realmからデータ取得
        val taskMonday = fncTask().read(1)
        val taskTuesday = fncTask().read(2)
        val taskWednesday = fncTask().read(3)
        val taskThursday = fncTask().read(4)
        val taskFriday = fncTask().read(5)
        val taskSaturday = fncTask().read(6)
        val taskSunday = fncTask().read(7)
        val taskMemo = fncTask().read(8)

        //対応するidを取得
        val idMonday = fncTask().getId(1)
        val idTuesday = fncTask().getId(2)
        val idWednesday = fncTask().getId(3)
        val idThursday = fncTask().getId(4)
        val idFriday = fncTask().getId(5)
        val idSaturday = fncTask().getId(6)
        val idSunday = fncTask().getId(7)
        val idMemo = fncTask().getId(8)

        // ArrayAdapterの生成
        val moAdapter = cAdapter(this, taskMonday, idMonday, 1)
        val tuAdapter = cAdapter(this, taskTuesday, idTuesday, 2)
        val weAdapter = cAdapter(this, taskWednesday, idWednesday, 3)
        val thAdapter = cAdapter(this, taskThursday, idThursday, 4)
        val frAdapter = cAdapter(this, taskFriday, idFriday, 5)
        val saAdapter = cAdapter(this, taskSaturday, idSaturday, 6)
        val suAdapter = cAdapter(this, taskSunday, idSunday, 7)
        val meAdapter = cAdapter(this, taskMemo, idMemo, 8)

        // ListViewに、生成したAdapterを設定
        listView_monday.adapter = moAdapter
        listView_tuesday.adapter = tuAdapter
        listView_wednesday.adapter = weAdapter
        listView_thursday.adapter = thAdapter
        listView_friday.adapter = frAdapter
        listView_saturday.adapter = saAdapter
        listView_sunday.adapter = suAdapter
        listView_memo.adapter = meAdapter

        //ListViewをスクロールさせない
        Utility().setListViewHeightBasedOnChildren(listView_monday)
        Utility().setListViewHeightBasedOnChildren(listView_tuesday)
        Utility().setListViewHeightBasedOnChildren(listView_wednesday)
        Utility().setListViewHeightBasedOnChildren(listView_thursday)
        Utility().setListViewHeightBasedOnChildren(listView_friday)
        Utility().setListViewHeightBasedOnChildren(listView_saturday)
        Utility().setListViewHeightBasedOnChildren(listView_sunday)
        Utility().setListViewHeightBasedOnChildren(listView_memo)

        //高さ初期化
        heightInit(linear_monday, textView_monday, listView_monday)
        heightInit(linear_tuesday, textView_tuesday, listView_tuesday)
        heightInit(linear_wednesday, textView_wednesday, listView_wednesday)
        heightInit(linear_thursday, textView_thursday, listView_thursday)
        heightInit(linear_friday, textView_friday, listView_friday)
        heightInit(linear_saturday, textView_saturday, listView_saturday)
        heightInit(linear_sunday, textView_sunday, listView_sunday)
        heightInit(linear_memo, textView_memo, listView_memo)

        //その日の曜日をハイライト
        val week = getCurrentWeek()
        when(week) {
            1 -> { textView_monday.setTextColor(Color.parseColor("#ff45ff")) }
            2 -> { textView_tuesday.setTextColor(Color.parseColor("#ff45ff")) }
            3 -> { textView_wednesday.setTextColor(Color.parseColor("#ff45ff")) }
            4 -> { textView_thursday.setTextColor(Color.parseColor("#ff45ff")) }
            5 -> { textView_friday.setTextColor(Color.parseColor("#ff45ff")) }
            6 -> { textView_saturday.setTextColor(Color.parseColor("#ff45ff")) }
            7 -> { textView_sunday.setTextColor(Color.parseColor("#ff45ff")) }
        }


    }

    //高さ初期値
    fun heightInit(linear: LinearLayout, tv: TextView, lv: ListView) {
        linear.afterMeasured {
            val liParam = linear.layoutParams
            val tvHeight = tv.height
            val lvHeight = lv.height
            liParam.height = tvHeight

            when(linear){
                linear_monday -> {
                    moTH = tvHeight
                    moLH = lvHeight
                }
                linear_tuesday -> {
                    tuTH = tvHeight
                    tuLH = lvHeight
                }
                linear_wednesday -> {
                    weTH = tvHeight
                    weLH = lvHeight
                }
                linear_thursday -> {
                    thTH = tvHeight
                    thLH = lvHeight
                }
                linear_friday -> {
                    frTH = tvHeight
                    frLH = lvHeight
                }
                linear_saturday -> {
                    saTH = tvHeight
                    saLH = lvHeight
                }
                linear_sunday -> {
                    suTH = tvHeight
                    suLH = lvHeight
                }
                linear_memo -> {
                    meTH = tvHeight
                    meLH = lvHeight
                }
            }
        }

    }

    //高さ変更
    fun heightChange(linear: LinearLayout, tv: TextView) {
        val liParam = linear.layoutParams
        val tvdp = changeUnit().pxToDp(tv.height, applicationContext).toInt() + 2
        val lidp = changeUnit().pxToDp(liParam.height, applicationContext).toInt()

        val hAnime: HeightAnimation = HeightAnimation()
        hAnime.duration = 150

        if(lidp == tvdp) {
            when(linear){
                linear_monday -> {hAnime.HeightAnimation(linear, moTH, moTH + moLH)}
                linear_tuesday -> {hAnime.HeightAnimation(linear, tuTH, tuTH + tuLH)}
                linear_wednesday -> {hAnime.HeightAnimation(linear, weTH, weTH + weLH)}
                linear_thursday -> {hAnime.HeightAnimation(linear, thTH, thTH + thLH)}
                linear_friday -> {hAnime.HeightAnimation(linear, frTH, frTH + frLH)}
                linear_saturday -> {hAnime.HeightAnimation(linear, saTH, saTH + saLH)}
                linear_sunday -> {hAnime.HeightAnimation(linear, suTH, suTH + suLH)}
                linear_memo -> {hAnime.HeightAnimation(linear, meTH, meTH + meLH)}
            }
        } else if(lidp != tvdp) {
            when(linear){
                linear_monday -> {hAnime.HeightAnimation(linear,moTH + moLH, moTH)}
                linear_tuesday -> {hAnime.HeightAnimation(linear,tuTH + tuLH, tuTH)}
                linear_wednesday -> {hAnime.HeightAnimation(linear,weTH + weLH,  weTH)}
                linear_thursday -> {hAnime.HeightAnimation(linear,thTH + thLH,  thTH)}
                linear_friday -> {hAnime.HeightAnimation(linear,frTH + frLH,  frTH)}
                linear_saturday -> {hAnime.HeightAnimation(linear,saTH + saLH,  saTH)}
                linear_sunday -> {hAnime.HeightAnimation(linear,suTH + suLH,  suTH)}
                linear_memo -> {hAnime.HeightAnimation(linear,meTH + meLH,  meTH)}
            }
        }

        linear.startAnimation(hAnime)

    }

    /**
     * 現在の曜日取得
     */
    fun getCurrentWeek(): Int {
        val calendar = Calendar.getInstance()
        var cWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
        return cWeek
    }

    /**
     * リスナーをセット
     */
    fun setListener() {
        //月曜日押下時
        listView_monday.setOnItemClickListener { parent, view, position, id ->
            val strTask = view.textView_task.text.toString()
            val fFlg = fncTask().getFixFlg(1, strTask)
            setDeleteDialogArg("月曜日", strTask, fFlg)
        }
        //火曜日押下時
        listView_tuesday.setOnItemClickListener { parent, view, position, id ->
            val strTask = view.textView_task.text.toString()
            val fFlg = fncTask().getFixFlg(2, strTask)
            setDeleteDialogArg("火曜日", strTask, fFlg)
        }
        //水曜日押下時
        listView_wednesday.setOnItemClickListener { parent, view, position, id ->
            val strTask = view.textView_task.text.toString()
            val fFlg = fncTask().getFixFlg(3, strTask)
            setDeleteDialogArg("水曜日", strTask, fFlg)
        }
        //木曜日押下時
        listView_thursday.setOnItemClickListener { parent, view, position, id ->
            val strTask = view.textView_task.text.toString()
            val fFlg = fncTask().getFixFlg(4, strTask)
            setDeleteDialogArg("木曜日", strTask, fFlg)
        }
        //金曜日押下
        listView_friday.setOnItemClickListener { parent, view, position, id ->
            val strTask = view.textView_task.text.toString()
            val fFlg = fncTask().getFixFlg(5, strTask)
            setDeleteDialogArg("金曜日", strTask, fFlg)
        }
        //土曜日押下時
        listView_saturday.setOnItemClickListener { parent, view, position, id ->
            val strTask = view.textView_task.text.toString()
            val fFlg = fncTask().getFixFlg(6, strTask)
            setDeleteDialogArg("土曜日", strTask, fFlg)
        }
        //日曜日押下時
        listView_sunday.setOnItemClickListener { parent, view, position, id ->
            val strTask = view.textView_task.text.toString()
            val fFlg = fncTask().getFixFlg(7, strTask)
            setDeleteDialogArg("日曜日", strTask, fFlg)
        }
        //メモ押下時
        listView_memo.setOnItemClickListener { parent, view, position, id ->
            val strTask = view.textView_task.text.toString()
            val fFlg = fncTask().getFixFlg(8, strTask)
            setDeleteDialogArg("メモ", strTask, fFlg)
        }

        //TextView押下時
        textView_monday.setOnClickListener { heightChange(linear_monday, textView_monday) }
        textView_tuesday.setOnClickListener { heightChange(linear_tuesday, textView_tuesday) }
        textView_wednesday.setOnClickListener { heightChange(linear_wednesday, textView_wednesday) }
        textView_thursday.setOnClickListener { heightChange(linear_thursday, textView_thursday) }
        textView_friday.setOnClickListener { heightChange(linear_friday, textView_friday) }
        textView_saturday.setOnClickListener { heightChange(linear_saturday, textView_saturday) }
        textView_sunday.setOnClickListener { heightChange(linear_sunday, textView_sunday) }
        textView_memo.setOnClickListener { heightChange(linear_memo, textView_memo) }

        //追加ボタン押下時
        button_add.setOnClickListener {
            //ダイアログ表示
            val dialog = AddFragment()
            dialog.show(supportFragmentManager, "simple")
        }

        //達成率画面へ遷移
        button_achievement.setOnClickListener {
            val intent = Intent(this, GraphActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

    }

    /**
     * 月曜になったら達成率を計算
     */
    fun calcAchieve(){
        //月曜の00時00分に処理を開始
        val getTest: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 15)
        }

        val alarmMgr = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(applicationContext, BroadCastReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(applicationContext, 0, intent, 0)
        }

        alarmMgr?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            getTest.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )
    }

    /**
     * deleteダイアログargumentsセットして表示
     */
    fun setDeleteDialogArg(argDt: String, argTask: String, argFlg: Int) {
        val args = Bundle()
        args.putString("dType", argDt)
        args.putString("task", argTask)
        args.putInt("fixFlg", argFlg)
        //ダイアログ表示
        val deDialog = DeleteFragment()
        deDialog.arguments = args
        deDialog.show(supportFragmentManager, "delete")
    }

    override fun onDestroy() {
        super.onDestroy()
        // Realmオブジェクトの削除
        realmTask.close()
        realmDone.close()
    }

}
