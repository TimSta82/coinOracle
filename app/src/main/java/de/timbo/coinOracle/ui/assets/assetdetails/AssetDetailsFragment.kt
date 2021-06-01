package de.timbo.coinOracle.ui.assets.assetdetails

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import de.timbo.coinOracle.R
import de.timbo.coinOracle.databinding.FragmentAssetDetailsBinding
import de.timbo.coinOracle.model.AssetHistory
import de.timbo.coinOracle.ui.BaseFragment
import de.timbo.coinOracle.utils.viewBinding
import java.math.RoundingMode
import java.sql.Date
import java.text.DecimalFormat

class AssetDetailsFragment : BaseFragment(R.layout.fragment_asset_details) {

    private val binding by viewBinding(FragmentAssetDetailsBinding::bind)
    private val navArgs by navArgs<AssetDetailsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.assetDetailsTv.text = navArgs.assetDetails.asset.name


        val assetHistoryList = navArgs.assetDetails.assetHistory.assetHistory?.map {
            if (it != null) AssetHistory(roundOffDecimal(it.priceUsd?.toDouble() ?: -1.00), "", convertLongToTime(it.time ?: -1L)) else AssetHistory("-1.00", "", convertLongToTime(-1L))
        } ?: listOf(AssetHistory("-1.00", "", convertLongToTime(-1L)))

        binding.assetDetailsHistoryTv.text = assetHistoryList.toString()
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