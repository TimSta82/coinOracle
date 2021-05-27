package de.timbo.coinOracle.injection

import de.timbo.coinOracle.utils.CameraUtils
import de.timbo.coinOracle.utils.ImageUtils
import org.koin.dsl.module

val helperModule = module {

    factory { CameraUtils() }
    factory { ImageUtils() }
}
