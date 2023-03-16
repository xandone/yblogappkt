package com.app.xandone.yblogapp.ui.manager.chart

import android.graphics.Color
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.xandone.baselib.utils.SimpleUtils.isEmpty
import com.app.xandone.yblogapp.App
import com.app.xandone.yblogapp.R
import com.app.xandone.yblogapp.base.BaseWallActivity
import com.app.xandone.yblogapp.cache.UserInfoHelper
import com.app.xandone.yblogapp.ui.manager.ManagerModelFactory
import com.app.xandone.yblogapp.utils.LineValueFormatter
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.Utils
import java.util.*

import kotlinx.android.synthetic.main.act_chart_data.*
import kotlinx.coroutines.launch

/**
 * author: Admin
 * created on: 2020/10/21 16:46
 * description:
 */
class ChartDataActivity : BaseWallActivity(), OnChartValueSelectedListener {

    private var mLineData: LineData? = null

    private val managerChartModel by lazy {
        ViewModelProvider(this, ManagerChartModelFactory()).get(
            ManagerChartModel::class.java
        )
    }


    override fun getLayout(): Int {
        return R.layout.act_chart_data
    }

    override fun wallInit() {
        initChart()

        managerChartModel.datas.observe(this) {
            val list1: MutableList<Int> = ArrayList()
            val list2: MutableList<Int> = ArrayList()
            val list3: MutableList<Int> = ArrayList()
            for (yearArtDataBean in it.data?.yearArtData!!) {
                list1.add(yearArtDataBean.codeCount)
                list2.add(yearArtDataBean.essayCount)
                list3.add(yearArtDataBean.year!!.toInt())
            }
            addSetData(
                list1,
                list3,
                "lable_1",
                ContextCompat.getColor(App.sContext!!, R.color.chart_fill_color1),
                true,
                R.drawable.fade_fill_blue
            )
            addSetData(
                list2,
                list3,
                "lable_2",
                ContextCompat.getColor(App.sContext!!, R.color.chart_fill_color3),
                true,
                R.drawable.fade_fill_yellow
            )
            chart1.invalidate()
            onLoadFinish()
        }

        lifecycleScope.launch { UserInfoHelper.adminId?.let { managerChartModel.getArtInfoData(it) } }
    }

    override fun requestData() {

    }

    private fun initChart() {
        chart1.setViewPortOffsets(50f, 40f, 10f, 20f)
        //        chart.setBackgroundColor(Color.rgb(104, 241, 175));
        chart1.setBackgroundColor(Color.WHITE)
        // no description text
        chart1.description.isEnabled = false

        // enable touch gestures
        chart1.setTouchEnabled(true)

        // enable scaling and dragging
        chart1.isDragEnabled = true
        chart1.setScaleEnabled(true)

        // if disabled, scaling can be done on x- and y-axis separately
        chart1.setPinchZoom(false)
        chart1.setDrawGridBackground(false)
        chart1.maxHighlightDistance = 300f
        //        chart1.setOnChartValueSelectedListener(this);
        val x = chart1.xAxis
        x.isEnabled = false
        val axisLeft = chart1.axisLeft
        axisLeft.setLabelCount(6, true)
        axisLeft.textColor = Color.BLACK
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        axisLeft.setDrawGridLines(true)
        axisLeft.axisLineColor = Color.WHITE
        axisLeft.yOffset = -6f
        axisLeft.setCenterAxisLabels(false)
        chart1.axisRight.isEnabled = false
        chart1.legend.isEnabled = false
        chart1.animateXY(1500, 1500)
    }

    /**
     * 添加表
     *
     * @param dList
     * @param lable
     * @param color
     * @param isMainValue
     */
    private fun addSetData(
        dList: List<Int>,
        yearList: List<Int>,
        lable: String,
        color: Int,
        isMainValue: Boolean,
        @DrawableRes fillDrawable: Int
    ) {
        if (isEmpty(dList)) {
            return
        }
        val values =
            ArrayList<Entry>()
        for (i in dList.indices) {
            val `val` = dList[i]
            values.add(Entry(i.toFloat(), `val`.toFloat()))
        }
        val set1 = LineDataSet(values, lable)
        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        set1.cubicIntensity = 0.2f
        set1.setDrawCircles(false)
        set1.lineWidth = 1.8f
        set1.circleRadius = 4f
        set1.setCircleColor(color)
        set1.highLightColor = Color.BLACK
        set1.color = color
        if (isMainValue) {
            set1.setDrawFilled(true)
        }
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            val drawable =
                ContextCompat.getDrawable(App.sContext!!, fillDrawable)
            set1.fillDrawable = drawable
        } else {
            set1.fillColor = ContextCompat.getColor(
                App.sContext!!,
                R.color.chart_fill_color1
            )
        }
        set1.fillAlpha = 50
        set1.setDrawHorizontalHighlightIndicator(false)
        set1.fillFormatter =
            IFillFormatter { dataSet, dataProvider -> chart1.axisLeft.axisMinimum }
        set1.valueFormatter = LineValueFormatter(dList, yearList)

//        XAxis xAxis = chart1.getXAxis();
//        xAxis.setValueFormatter(new LineValueFormatter(dList, yearList));
        if (mLineData == null) {
            mLineData = LineData()
        }
        mLineData!!.addDataSet(set1)
        mLineData!!.setValueTextSize(9f)
        //是否绘制顶点数值
        mLineData!!.setDrawValues(false)
        // set data
        chart1.data = mLineData
    }

    override fun onValueSelected(e: Entry, h: Highlight) {
    }

    override fun onNothingSelected() {}
}