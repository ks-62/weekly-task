package com.example.WeeklyTask.Fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.example.WeeklyTask.Activity.TaskActivity
import com.example.WeeklyTask.Realm.fncTask
import com.example.weeklytask.R
import kotlinx.android.synthetic.main.dialog_update.view.*

class UpdateFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        //テキスト変更
        val inputDType = arguments!!.getString("dType")
        val inputTask = arguments!!.getString("task")

        var upDataType = 0

        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_update, null)

        view.editText_task.setText(inputTask)

        if(arguments!!.getInt("fixFlg") == 1) {
            view.chFixFlg.isChecked = true
        }

        if(arguments?.getString("textErr") != null){
            view.textView_err.text = arguments!!.getString("textErr")
        }

        when(inputDType){
            "月曜日" -> {
                view.spnrDataType.setSelection(0)
                upDataType = 1
            }
            "火曜日" -> {
                view.spnrDataType.setSelection(1)
                upDataType = 2
            }
            "水曜日" -> {
                view.spnrDataType.setSelection(2)
                upDataType = 3
            }
            "木曜日" -> {
                view.spnrDataType.setSelection(3)
                upDataType = 4
            }
            "金曜日" -> {
                view.spnrDataType.setSelection(4)
                upDataType = 5
            }
            "土曜日" -> {
                view.spnrDataType.setSelection(5)
                upDataType = 6
            }
            "日曜日" -> {
                view.spnrDataType.setSelection(6)
                upDataType = 7
            }
            "メモ" -> {
                view.spnrDataType.setSelection(7)
                upDataType = 8
            }
        }

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .setPositiveButton("更新", DialogInterface.OnClickListener { dialog, which ->
                //選択された値を取得
                val upDtText = view.spnrDataType.selectedItem.toString()
                val upTask = view.editText_task.text.toString()
                var upDt = 0
                val upFFlg = view.chFixFlg
                var intFFlg: Int = 0
                if(upFFlg.isChecked){
                    intFFlg = 1
                } else if(!upFFlg.isChecked){
                    intFFlg = 0
                }

                when(upDtText) {
                    "月曜日" -> { upDt = 1 }
                    "火曜日" -> { upDt = 2 }
                    "水曜日" -> { upDt = 3 }
                    "木曜日" -> { upDt = 4 }
                    "金曜日" -> { upDt = 5 }
                    "土曜日" -> { upDt = 6 }
                    "日曜日" -> { upDt = 7 }
                    "メモ" -> { upDt = 8}
                }

                if(fncTask().update(upDataType, inputTask, upDt, upTask, intFFlg)){
                    //ページ再読み込み
                    TaskActivity().finish()
                    val intent = Intent(activity, TaskActivity::class.java)
                    startActivity(intent)
                    activity!!.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                } else {
                    val args = Bundle()
                    args.putString("textErr", "すでに登録されている課題は登録できません")
                    //ダイアログ表示
                    val dialog = UpdateFragment()
                    dialog.arguments = args
                    dialog.show(activity!!.supportFragmentManager, "upDate")
                }

            })
            .setNegativeButton("キャンセル", DialogInterface.OnClickListener { dialog, which ->
                dialog.cancel()
            })
            .create()

    }
}