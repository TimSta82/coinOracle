package de.timbo.coinOracle.ui.assets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.timbo.coinOracle.databinding.ListItemAssetBinding
import de.timbo.coinOracle.model.Asset

class AssetsAdapter(private val assets: List<Asset>, private val onAssetClick: (Asset) -> Unit) : RecyclerView.Adapter<AssetsAdapter.AssetsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetsViewHolder {
        return AssetsViewHolder(ListItemAssetBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: AssetsViewHolder, position: Int) {
        holder.bind(assets[position])
    }

    override fun getItemCount() = assets.size

    inner class AssetsViewHolder(private val binding: ListItemAssetBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(asset: Asset) {
            binding.itemAssetTitleTv.text = "(${asset.symbol}) - ${asset.name} - ${asset.priceEuro}€"
            binding.root.setOnClickListener {
                onAssetClick(asset)
            }
        }
    }
}
