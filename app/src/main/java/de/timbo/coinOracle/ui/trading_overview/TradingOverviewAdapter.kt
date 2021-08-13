package de.timbo.coinOracle.ui.trading_overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.timbo.coinOracle.R
import de.timbo.coinOracle.database.model.TradeEntity
import de.timbo.coinOracle.databinding.ListItemTradeBinding
import de.timbo.coinOracle.extensions.convertLongToTime
import de.timbo.coinOracle.extensions.getColorStateListOneColor
import de.timbo.coinOracle.extensions.roundOffDecimal

class TradingOverviewAdapter(private val trades: List<TradeEntity>, private val onTradeClick: (TradeEntity) -> Unit) : RecyclerView.Adapter<TradingOverviewAdapter.TradingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradingViewHolder {
        return TradingViewHolder(ListItemTradeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TradingViewHolder, position: Int) {
        holder.bind(trades[position])
    }

    override fun getItemCount() = trades.size

    inner class TradingViewHolder(private val binding: ListItemTradeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(trade: TradeEntity) {
            trade.isSold.let {
                binding.root.backgroundTintList = itemView.context.getColorStateListOneColor(if (it) R.color.trading_sold else R.color.trading_bought)
            }
            binding.itemTradeSymbolTv.text = trade.assetSymbol
            binding.itemTradeTitleTv.text = trade.assetId
            binding.itemTradeAssetValueTv.text = "Asset value: ${trade.assetValue.toDouble().roundOffDecimal()}€"
            binding.itemTradeAssetAmountTv.text = "Amount: ${trade.amount.toString()}"
            binding.itemTradeAssetPriceTv.text = "Price: ${(trade.amount * trade.assetValue.toDouble()).roundOffDecimal()}€"
            binding.itemTradeDateTv.text = "Date: ${trade.timeStamp.convertLongToTime()}"
            binding.root.setOnClickListener {
                onTradeClick(trade)
            }
        }
    }
}