package de.timbo.coracle.injection

import de.timbo.coracle.utils.CameraUtils
import de.timbo.coracle.utils.ImageUtils
import org.koin.dsl.module

val helperModule = module {

    factory { CameraUtils() }
    factory { ImageUtils() }
}
