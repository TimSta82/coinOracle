package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.repositories.AssetsRepository
import org.koin.core.component.inject

class SaveAssetsUseCase : BaseUseCase() {

    private val assetsRepository by inject<AssetsRepository>()

    fun call(assets: List<Asset>) = assetsRepository.saveAssets(assets)
}
