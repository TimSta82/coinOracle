package de.timbo.coinOracle.ui.correlation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.timbo.coinOracle.R
import de.timbo.coinOracle.databinding.ListItemCorrelationBinding
import de.timbo.coinOracle.extensions.getColorStateListOneColor
import de.timbo.coinOracle.extensions.roundOffDecimal
import de.timbo.coinOracle.model.CorrelatingAssets

class CorrelationAdapter(
    private val onClick: (CorrelatingAssets) -> Unit
) : ListAdapter<CorrelatingAssets, CorrelationViewHolder>(CorrelationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CorrelationViewHolder =
        CorrelationViewHolder(ListItemCorrelationBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: CorrelationViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }
}

class CorrelationViewHolder(private val itemBinding: ListItemCorrelationBinding) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(correlatingAssets: CorrelatingAssets, onClick: (CorrelatingAssets) -> Unit) {
        correlatingAssets.winnerAsset.let { asset ->
            itemBinding.itemWinnerSymbolTv.text = asset.symbol
            itemBinding.itemWinnerSymbolTv.setTextColor(itemView.context.getColorStateListOneColor(R.color.correlation_winner))
            itemBinding.itemWinnerPercentageTv.text = "${asset.changePercent24Hr.toDouble().roundOffDecimal()}%"
            itemBinding.itemWinnerPercentageTv.setTextColor(itemView.context.getColorStateListOneColor(R.color.correlation_winner))
        }
        correlatingAssets.loserAsset.let { asset ->
            itemBinding.itemLoserSymbolTv.text = asset.symbol
            itemBinding.itemLoserSymbolTv.setTextColor(itemView.context.getColorStateListOneColor(R.color.correlation_loser))
            itemBinding.itemLoserPercentageTv.text = "${asset.changePercent24Hr.toDouble().roundOffDecimal()}%"
            itemBinding.itemLoserPercentageTv.setTextColor(itemView.context.getColorStateListOneColor(R.color.correlation_loser))
        }
        itemBinding.root.setOnClickListener {
            onClick(correlatingAssets)
        }
    }
}

class CorrelationDiffCallback : DiffUtil.ItemCallback<CorrelatingAssets>() {
    override fun areItemsTheSame(oldItem: CorrelatingAssets, newItem: CorrelatingAssets) =
        oldItem.winnerAsset.id == newItem.winnerAsset.id &&
            oldItem.loserAsset.id == newItem.loserAsset.id

    override fun areContentsTheSame(oldItem: CorrelatingAssets, newItem: CorrelatingAssets) =
        oldItem.winnerAsset.symbol == newItem.winnerAsset.symbol &&
            oldItem.loserAsset.symbol == newItem.loserAsset.symbol &&
            oldItem.winnerAsset.changePercent24Hr == newItem.winnerAsset.changePercent24Hr &&
            oldItem.loserAsset.changePercent24Hr == newItem.loserAsset.changePercent24Hr
}
