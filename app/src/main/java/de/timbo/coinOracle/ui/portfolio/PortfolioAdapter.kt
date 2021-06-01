package de.timbo.coinOracle.ui.portfolio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.timbo.coinOracle.databinding.ListItemPortfolioMyassetBinding
import de.timbo.coinOracle.model.MyAsset

class PortfolioAdapter(private val myAssets: List<MyAsset>, private val onAssetClick: (MyAsset) -> Unit) : RecyclerView.Adapter<PortfolioAdapter.PortfolioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioViewHolder {
        return PortfolioViewHolder(ListItemPortfolioMyassetBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PortfolioViewHolder, position: Int) {
        holder.bind(myAssets[position])
    }

    override fun getItemCount() = myAssets.size

    inner class PortfolioViewHolder(private val binding: ListItemPortfolioMyassetBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(myAsset: MyAsset) {
            binding.itemPortfolioAssetSymbolTv.text = myAsset.symbol
            binding.itemPortfolioAssetTitleTv.text = myAsset.name
            binding.itemPortfolioAssetValueTv.text = myAsset.purchasePriceEur
            binding.itemPortfolioAssetAmountTv.text = "Amount: ${myAsset.amount}"
            binding.itemPortfolioAssetCumulatedValueTv.text = "total value: ${(myAsset.amount * myAsset.purchasePriceEur.toDouble())}€"
            binding.root.setOnClickListener {
                onAssetClick(myAsset)
            }
        }
    }
}