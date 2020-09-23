package com.example.WeeklyTask.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.WeeklyTask.Realm.fncDone
import com.example.weeklytask.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlinx.android.synthetic.main.activity_graph.*

class GraphActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        val chart = bar_chart

        //表示データ読み取り
        val doneList: MutableList<Int> = fncDone().read()

        //データが8個未満の場合は8個にする
        while (doneList.size < 8) {
            doneList.add(0, 0)
        }

        //表示データ取得
        chart.data = BarData(getBarData(doneList))
        //chart.data = BarData(getBarData(doneList))

        //Y軸(左)の設定
        chart.axisLeft.apply {
            axisMinimum = 0f
            axisMaximum = 100f
            labelCount = 5
            setDrawTopYLabelEntry(true)
            setValueFormatter { value, axis -> "" + value.toInt() + "%"}
        }

        //Y軸(右)の設定
        chart.axisRight.apply {
            setDrawLabels(false)
            setDrawGridLines(false)
            setDrawZeroLine(false)
            setDrawTopYLabelEntry(true)
        }

        //X軸の設定
        val l: Int = doneList.count()
        when(l){
            0 ->{
                val labels = arrayOf("8週間前", "7週間前", "6週間前", "5週間前", "4週間前", "3週間前", "2週間前", "先週")
                chart.xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(labels)
                    labelCount = 8 //表示させるラベル数
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawLabels(true)
                    setDrawGridLines(false)
                    setDrawAxisLine(true)
                }
            }
            1 ->{
                val labels = arrayOf("先週")
                chart.xAxis.apply {
                    //setValueFormatter { labels, axis -> labels.toString()}
                    valueFormatter = IndexAxisValueFormatter(labels)
                    //setLabelCount(1, true)
                    labelCount = 1 //表示させるラベル数
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawLabels(true)
                    setDrawGridLines(false)
                    setDrawAxisLine(true)
                }
            }
            2 ->{
                val labels = arrayOf("2週間前", "先週")
                chart.xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(labels)
                    labelCount = 2 //表示させるラベル数
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawLabels(true)
                    setDrawGridLines(false)
                    setDrawAxisLine(true)
                }
            }
            3 ->{
                val labels = arrayOf("3週間前", "2週間前", "先週")
                chart.xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(labels)
                    labelCount = 3 //表示させるラベル数
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawLabels(true)
                    setDrawGridLines(false)
                    setDrawAxisLine(true)
                }
            }
            4 ->{
                val labels = arrayOf("4週間前", "3週間前", "2週間前", "先週")
                chart.xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(labels)
                    labelCount = 4 //表示させるラベル数
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawLabels(true)
                    setDrawGridLines(false)
                    setDrawAxisLine(true)
                }
            }
            5 ->{
                val labels = arrayOf("5週間前", "4週間前", "3週間前", "2週間前", "先週")
                chart.xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(labels)
                    labelCount = 5 //表示させるラベル数
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawLabels(true)
                    setDrawGridLines(false)
                    setDrawAxisLine(true)
                }
            }
            6 ->{
                val labels = arrayOf("6週間前", "5週間前", "4週間前", "3週間前", "2週間前", "先週")
                chart.xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(labels)
                    labelCount = 6 //表示させるラベル数
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawLabels(true)
                    setDrawGridLines(false)
                    setDrawAxisLine(true)
                }
            }
            7 ->{
                val labels = arrayOf("7週間前", "6週間前", "5週間前", "4週間前", "3週間前", "2週間前", "先週")
                chart.xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(labels)
                    labelCount = 7 //表示させるラベル数
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawLabels(true)
                    setDrawGridLines(false)
                    setDrawAxisLine(true)
                }
            }
            8 ->{
                val labels = arrayOf("8週間前", "7週間前", "6週間前", "5週間前", "4週間前", "3週間前", "2週間前", "先週")
                chart.xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(labels)
                    labelCount = 8 //表示させるラベル数
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawLabels(true)
                    setDrawGridLines(false)
                    setDrawAxisLine(true)
                }
            }
        }

        //グラフ上の表示
        chart.apply {
            setDrawValueAboveBar(true)
            description.isEnabled = false
            isClickable = false
            legend.isEnabled = false //凡例
            setScaleEnabled(false)
            animateY(1200, Easing.EasingOption.Linear)
            barData.barWidth = 0.6f
        }

        setListener()

    }

    fun setListener() {
        button_back.setOnClickListener {
            val intent = Intent(this, TaskActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private fun getBarData(graphData: MutableList<Int>): ArrayList<IBarDataSet> {

        var entries = ArrayList<BarEntry>().apply {
            graphData.forEachIndexed { index, i ->
                this.add(BarEntry(index.toFloat(), i.toFloat()))
            }
        }

        val dataSet = BarDataSet(entries, "bar").apply {
            //整数で表示
            valueFormatter = IValueFormatter { value, _, _, _ -> "" + value.toInt() }
            //ハイライトさせない
            isHighlightEnabled = false

        }


        val bars = ArrayList<IBarDataSet>()
        bars.add(dataSet)
        return bars
    }

}
