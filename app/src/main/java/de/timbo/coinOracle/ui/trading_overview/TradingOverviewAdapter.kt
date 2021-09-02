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

class TradingOverviewAdapter(private val onTradeClick: (TradedAssetWithCurrentValue) -> Unit) : RecyclerView.Adapter<TradingOverviewAdapter.TradingViewHolder>() {

    private var trades: List<TradedAssetWithCurrentValue> = emptyList()

    fun setTrades(trades: List<TradedAssetWithCurrentValue>) {
        this.trades = trades
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradingViewHolder {
        return TradingViewHolder(ListItemTradeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TradingViewHolder, position: Int) {
        holder.bind(trades[position])
    }

    override fun getItemCount() = trades.size

    inner class TradingViewHolder(private val binding: ListItemTradeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tradedAssetWithCurrentValue: TradedAssetWithCurrentValue) {
            tradedAssetWithCurrentValue.tradeEntity.isSold.let { isSold ->
                binding.root.backgroundTintList = itemView.context.getColorStateListOneColor(if (isSold) R.color.trading_sold else R.color.trading_bought)
            }
            binding.itemTradeSymbolTv.text = tradedAssetWithCurrentValue.tradeEntity.assetSymbol
            binding.itemTradeTitleTv.text = tradedAssetWithCurrentValue.tradeEntity.assetId
            binding.itemTradeAssetPurchaseValueTv.text = "PP: ${tradedAssetWithCurrentValue.tradeEntity.assetValue.toDouble().roundOffDecimal()}€"
            binding.itemTradeAssetCurrentValueTv.text = "CER: ${tradedAssetWithCurrentValue.assetCurrentValue.priceEuro.toDouble().roundOffDecimal()}€"
            binding.itemTradeAssetAmountTv.text = "Amount: ${tradedAssetWithCurrentValue.tradeEntity.amount}"
            binding.itemTradeAssetPriceTv.text = "Total price: ${(tradedAssetWithCurrentValue.tradeEntity.amount * tradedAssetWithCurrentValue.tradeEntity.assetValue.toDouble()).roundOffDecimal()}€"
            binding.itemTradeDateTv.text = "Date: ${tradedAssetWithCurrentValue.tradeEntity.timeStamp.convertLongToTime()}"
            binding.root.setOnClickListener {
                onTradeClick(tradedAssetWithCurrentValue)
            }
        }
    }
}
