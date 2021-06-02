package de.timbo.coinOracle.ui.portfolio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.timbo.coinOracle.databinding.ListItemPortfolioMyassetBinding
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.model.MyAsset
import de.timbo.coinOracle.model.PortfolioWithCurrentAssetPrices

class PortfolioAdapter(private val portfolioWithCurrentAssetPrices: PortfolioWithCurrentAssetPrices, private val onAssetClick: (MyAsset) -> Unit) :
    RecyclerView.Adapter<PortfolioAdapter.PortfolioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioViewHolder {
        return PortfolioViewHolder(ListItemPortfolioMyassetBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PortfolioViewHolder, position: Int) {
        holder.bind(portfolioWithCurrentAssetPrices.portfolioEntity.myAssets[position])
    }

    override fun getItemCount() = portfolioWithCurrentAssetPrices.portfolioEntity.myAssets.size

    inner class PortfolioViewHolder(private val binding: ListItemPortfolioMyassetBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(myAsset: MyAsset) {
            val currentAsset = getCurrentAssetPrice(myAsset)
            binding.itemPortfolioAssetSymbolTv.text = myAsset.asset.symbol
            binding.itemPortfolioAssetTitleTv.text = myAsset.asset.name
            binding.itemPortfolioCurrentAssetValueTv.text = "${currentAsset.priceEuro}€"
            binding.itemPortfolioAssetAmountTv.text = "Amount: ${myAsset.amount}"
            binding.itemPortfolioAssetCumulatedValueTv.text = "total value: ${(myAsset.amount * currentAsset.priceEuro.toDouble())}€"
            binding.root.setOnClickListener {
                onAssetClick(myAsset)
            }
        }
    }

    private fun getCurrentAssetPrice(myAsset: MyAsset): Asset {
        val currentAsset = portfolioWithCurrentAssetPrices.currentAssets.find { currentAsset -> currentAsset.id == myAsset.asset.id }
        return currentAsset ?: myAsset.asset
    }
}
