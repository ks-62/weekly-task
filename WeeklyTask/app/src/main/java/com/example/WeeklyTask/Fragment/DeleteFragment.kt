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
import kotlinx.android.synthetic.main.dialog_delete.view.*

class DeleteFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_delete, null)

        //arguments取得
        val inputDType = arguments!!.getString("dType")
        val inputTask = arguments!!.getString("task")
        val inputFlg = arguments!!.getInt("fixFlg")
        //テキスト変更
        view.textView_week.text = inputDType
        view.textView_task.text = inputTask

        return AlertDialog.Builder(requireContext())
            .setView(view) // 作成したビューをコンテンツ領域に設定
            .setPositiveButton("削除", DialogInterface.OnClickListener{ _, _ ->

                when(inputDType) {
                    "月曜日" -> { fncTask().delete(1, inputTask) }
                    "火曜日" -> { fncTask().delete(2, inputTask) }
                    "水曜日" -> { fncTask().delete(3, inputTask) }
                    "木曜日" -> { fncTask().delete(4, inputTask) }
                    "金曜日" -> { fncTask().delete(5, inputTask) }
                    "土曜日" -> { fncTask().delete(6, inputTask) }
                    "日曜日" -> { fncTask().delete(7, inputTask) }
                    "メモ" -> { fncTask().delete(8, inputTask) }
                }

                //ページ再読み込み
                TaskActivity().finish()
                val intent = Intent(activity, TaskActivity::class.java)
                startActivity(intent)
                activity!!.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            })
            .setNegativeButton("変更", DialogInterface.OnClickListener { _, _ ->

                //渡す値を設定
                val args = Bundle()
                args.putString("dType", inputDType )
                args.putString("task", inputTask)
                args.putInt("fixFlg", inputFlg
                )
                //現在のダイアログを消して、EditTextのあるダイアログを表示
                dialog.cancel()
                val upDialog = UpdateFragment()
                upDialog.arguments = args
                upDialog.show(fragmentManager, "update")
            })
            .setNeutralButton("戻る", DialogInterface.OnClickListener { dialog, which ->
                dialog.cancel()
            })
            .create()
    }
}