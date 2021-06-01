package de.timbo.coinOracle.ui

import androidx.lifecycle.ViewModel
import de.timbo.coinOracle.usecases.GetAssetHistoryUseCase
import de.timbo.coinOracle.usecases.GetAssetsUseCase
import de.timbo.coinOracle.usecases.GetEuroRateUseCase
import de.timbo.coinOracle.usecases.SaveAssetsUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel: ViewModel(), KoinComponent {

    private val getAssetsUseCase by inject<GetAssetsUseCase>()
    private val getEuroRateUseCase by inject<GetEuroRateUseCase>()
    private val getAssetHistoryUseCase by inject<GetAssetHistoryUseCase>()
    private val saveAssetsUseCase by inject<SaveAssetsUseCase>()




}