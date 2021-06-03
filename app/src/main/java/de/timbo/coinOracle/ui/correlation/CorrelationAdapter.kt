package de.timbo.coinOracle.ui.correlation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.timbo.coinOracle.R
import de.timbo.coinOracle.database.model.CorrelationEntity
import de.timbo.coinOracle.databinding.ListItemCorrelationBinding
import de.timbo.coinOracle.extensions.getColorStateListOneColor

class CorrelationAdapter(
    private val onClick: (CorrelationEntity) -> Unit
) : ListAdapter<CorrelationEntity, CorrelationViewHolder>(CorrelationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CorrelationViewHolder =
        CorrelationViewHolder(ListItemCorrelationBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: CorrelationViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }
}

class CorrelationViewHolder(private val itemBinding: ListItemCorrelationBinding) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(correlationEntity: CorrelationEntity, onClick: (CorrelationEntity) -> Unit) {
        correlationEntity.winnerAsset.let { asset ->
            itemBinding.itemWinnerSymbolTv.text = asset.symbol
            itemBinding.itemWinnerSymbolTv.setTextColor(itemView.context.getColorStateListOneColor(R.color.correlation_winner))
            itemBinding.itemWinnerPercentageTv.text = asset.changePercent24Hr
            itemBinding.itemWinnerPercentageTv.setTextColor(itemView.context.getColorStateListOneColor(R.color.correlation_winner))
        }
        correlationEntity.loserAsset.let { asset ->
            itemBinding.itemLoserSymbolTv.text = asset.symbol
            itemBinding.itemLoserSymbolTv.setTextColor(itemView.context.getColorStateListOneColor(R.color.correlation_loser))
            itemBinding.itemLoserPercentageTv.text = asset.changePercent24Hr
            itemBinding.itemLoserPercentageTv.setTextColor(itemView.context.getColorStateListOneColor(R.color.correlation_loser))
        }
    }
}

class CorrelationDiffCallback : DiffUtil.ItemCallback<CorrelationEntity>() {
    override fun areItemsTheSame(oldItem: CorrelationEntity, newItem: CorrelationEntity) =
        oldItem.winnerAsset.id == newItem.winnerAsset.id &&
            oldItem.loserAsset.id == newItem.loserAsset.id

    override fun areContentsTheSame(oldItem: CorrelationEntity, newItem: CorrelationEntity) =
        oldItem.winnerAsset.symbol == newItem.winnerAsset.symbol &&
            oldItem.loserAsset.symbol == newItem.loserAsset.symbol &&
            oldItem.winnerAsset.changePercent24Hr == newItem.winnerAsset.changePercent24Hr &&
            oldItem.loserAsset.changePercent24Hr == newItem.loserAsset.changePercent24Hr

}
