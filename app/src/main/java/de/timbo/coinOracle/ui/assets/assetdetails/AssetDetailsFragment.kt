package de.timbo.coinOracle.ui.assets.assetdetails

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import de.timbo.coinOracle.R
import de.timbo.coinOracle.databinding.FragmentAssetDetailsBinding
import de.timbo.coinOracle.ui.BaseFragment
import de.timbo.coinOracle.utils.viewBinding
import java.math.RoundingMode
import java.sql.Date
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class AssetDetailsFragment : BaseFragment(R.layout.fragment_asset_details) {

    private val binding by viewBinding(FragmentAssetDetailsBinding::bind)
    private val navArgs by navArgs<AssetDetailsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.assetDetailsTv.text = navArgs.assetDetails.asset.name

        setChart()
        setData()

//        val assetHistoryList = navArgs.assetDetails.assetHistory.assetHistory?.map {
//            if (it != null) AssetHistory(roundOffDecimal(it.priceUsd?.toDouble() ?: -1.00), "", convertLongToTime(it.time ?: -1L)) else AssetHistory("-1.00", "", convertLongToTime(-1L))
//        } ?: listOf(AssetHistory("-1.00", "", convertLongToTime(-1L)))
    }

    private fun setChart() {
        val chart = binding.assetDetailsChart
        chart.setViewPortOffsets(0F, 0F, 1F, 0F)
        chart.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))

        chart.description.isEnabled = false
        chart.setTouchEnabled(true)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false)

        chart.setDrawGridBackground(false)
        chart.maxHighlightDistance = 300f

        // X AXIS
        val x = chart.xAxis
        x.isEnabled = false
        x.granularity = 1f
        x.setCenterAxisLabels(true)
        x.valueFormatter = object : ValueFormatter() {
            val format = SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH)
            override fun getFormattedValue(value: Float, axis: AxisBase?): String {
                val millis = TimeUnit.HOURS.toMillis(value.toLong())
                return format.format(Date(millis))
            }
        }


        // Y AXIS
        val y = chart.axisLeft
//        y.typeface = tfLight
        y.setLabelCount(6, false)
        y.textColor = Color.WHITE
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        y.setDrawGridLines(false)
        y.axisLineColor = Color.WHITE

        chart.axisRight.isEnabled = false

//        chart.legend.isEnabled = false

        chart.animateXY(2000, 2000)

        chart.invalidate()
    }

    fun setData() {
        var values = ArrayList<Entry>()
        navArgs.assetDetails.assetHistory.assetHistory?.forEach {
            if (it != null) values.add(Entry(it.time?.toFloat() ?: -1F, it.priceUsd?.toFloat() ?: -1F))
        }
        val set1 = LineDataSet(values, "${navArgs.assetDetails.asset.name}")
        set1.mode = LineDataSet.Mode.LINEAR
        set1.cubicIntensity = 0.2f

        val data = LineData(set1)

        binding.assetDetailsChart.data = data

        val legend = binding.assetDetailsChart.legend
        legend.isEnabled = true
    }

    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return format.format(date)
    }

    private fun roundOffDecimal(number: Double): String {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number)
    }
}