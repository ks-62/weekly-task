package com.example.WeeklyTask.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.example.WeeklyTask.Realm.fncTask
import com.example.weeklytask.R
import io.realm.Realm

class cAdapter(context: Context, var listTask: List<String>, var listId: MutableList<Int?>, val dataTypeNo: Int)
    : ArrayAdapter<String>(context, 0, listTask)  {

    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val cBtnDone: MutableList<Int> = mutableListOf()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        // TaskListの取得
        val crtListTask = listTask[position]
        val crtListId = listId[position]

        // レイアウトの設定
        var view = convertView
        if (convertView == null) {
            if(dataTypeNo != 8) {
                view = layoutInflater.inflate(R.layout.listview_with_button, parent, false)
            } else if(dataTypeNo == 8) {
                view = layoutInflater.inflate(R.layout.listview_without_button, parent, false)
            }
        }

        val btnDone = view!!.findViewById<Button>(R.id.button_done)


        //dTaskの中のデータを使って、dTaskのどのデータがdoneFlgが1かを取得する(idを使う)
        //そしたらdoneFlgが1のdTaskの時のみボタンの色を変える。
        val cDFlgD = fncTask().getDF1(crtListId)

        if(cDFlgD == crtListTask) {
            btnDone.setBackgroundColor(Color.parseColor("#00dbde"))
            btnDone.setTextColor(Color.parseColor("#FFFFFF"))
            btnDone.text = "✔︎"
        }

        //課題をテキストに反映
        val tTask = view?.findViewById<TextView>(R.id.textView_task)
        tTask?.text = crtListTask


        if(dataTypeNo != 8) {
            btnDone.setOnClickListener {
                if(btnDone.text == "✔︎") {
                    btnDone.setBackgroundResource(R.drawable.button_frame)
                    btnDone.setTextColor(Color.parseColor("#696969"))
                    btnDone.text = "完了"
                    val tdText = view.findViewById<TextView>(R.id.textView_task).text.toString()
                    fncTask().turnDF0(tdText, dataTypeNo)
                } else if(btnDone.text == "完了") {
                    btnDone.setBackgroundColor(Color.parseColor("#00dbde"))
                    btnDone.setTextColor(Color.parseColor("#FFFFFF"))
                    btnDone.text = "✔︎"
                    val tdText = view.findViewById<TextView>(R.id.textView_task).text.toString()
                    fncTask().turnDF1(tdText, dataTypeNo)
                }
            }
        }

        return view!!

    }



}