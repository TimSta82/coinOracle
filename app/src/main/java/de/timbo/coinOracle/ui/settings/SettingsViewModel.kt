package de.timbo.coinOracle.ui.settings

import androidx.lifecycle.ViewModel
import de.timbo.coinOracle.extensions.launch
import de.timbo.coinOracle.usecases.ResetEverythingUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsViewModel: ViewModel(), KoinComponent {

    private val resetEverythingUseCase by inject<ResetEverythingUseCase>()


    fun reset() {
        launch {
            resetEverythingUseCase.call()
        }
    }
}