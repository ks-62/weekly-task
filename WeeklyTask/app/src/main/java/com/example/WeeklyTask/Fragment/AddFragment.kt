package com.example.WeeklyTask.Fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.example.WeeklyTask.Activity.TaskActivity
import com.example.WeeklyTask.Realm.fncTask
import com.example.weeklytask.R
import kotlinx.android.synthetic.main.dialog_add.view.*

class AddFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add, null)

        if(arguments?.getString("textErr") != null){
            view.textView_err.text = arguments!!.getString("textErr")
        }

        return AlertDialog.Builder(requireContext())
            .setView(view) // 作成したビューをコンテンツ領域に設定
            .setPositiveButton("作成", DialogInterface.OnClickListener{ _, _ ->
                //OKボタン押下時
                val spnrDType = dialog.findViewById<Spinner>(R.id.spnrDataType).selectedItem.toString()
                val edtTask = dialog.findViewById<EditText>(R.id.editText_task).text.toString()
                var reDType: Int = 0

                when(spnrDType) {
                    "月曜日" -> { reDType = 1 }
                    "火曜日" -> { reDType = 2 }
                    "水曜日" -> { reDType = 3 }
                    "木曜日" -> { reDType = 4 }
                    "金曜日" -> { reDType = 5 }
                    "土曜日" -> { reDType = 6 }
                    "日曜日" -> { reDType = 7 }
                    "メモ" -> {reDType = 8}
                }

                var fFlgCheck: Int = 0
                val cfFlg = view.findViewById<CheckBox>(R.id.chFixFlg)
                if(cfFlg.isChecked){
                    fFlgCheck = 1
                } else if(!cfFlg.isChecked) {
                    fFlgCheck = 0
                }

                //データベースにインサート
                val intInsert = fncTask().insert(reDType, edtTask, fFlgCheck)

                if(intInsert == 2) {
                    //ページ再読み込み
                    TaskActivity().finish()
                    val intent = Intent(activity, TaskActivity::class.java)
                    startActivity(intent)
                    activity!!.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                } else if(intInsert == 0) {
                    val args = Bundle()
                    args.putString("textErr", "すでに登録されている課題は登録できません。")
                    //ダイアログ表示
                    val addDialog = AddFragment()
                    addDialog.arguments = args
                    addDialog.show(activity!!.supportFragmentManager, "simple")
                } else if(intInsert == 1) {
                    val args = Bundle()
                    args.putString("textErr", "一つの曜日に設定できる課題は10個までです。お休みも大切です。")
                    //ダイアログ表示
                    val addDialog = AddFragment()
                    addDialog.arguments = args
                    addDialog.show(activity!!.supportFragmentManager, "simple")
                }

            })
            .setNegativeButton("キャンセル", DialogInterface.OnClickListener { dialog, which ->
                dialog.cancel()
            })
            .create()
    }

}