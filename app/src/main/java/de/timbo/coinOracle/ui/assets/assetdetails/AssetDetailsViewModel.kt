package de.timbo.coinOracle.ui.assets.assetdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import de.timbo.coinOracle.utils.SingleLiveEvent

class AssetDetailsViewModel: ViewModel() {

    private val _onTradeButtonClicked = SingleLiveEvent<Any>()
    val onTradeButtonClicked : LiveData<Any> = _onTradeButtonClicked

    fun toggleTradeMenu() {
        _onTradeButtonClicked.call()
    }
}