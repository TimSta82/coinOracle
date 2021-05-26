package de.bornholdtlee.defaultprojectkotlin.injection

import de.bornholdtlee.defaultprojectkotlin.utils.CameraUtils
import de.bornholdtlee.defaultprojectkotlin.utils.ImageUtils
import org.koin.dsl.module

val helperModule = module {

    factory { CameraUtils() }
    factory { ImageUtils() }
}
