package de.bornholdtlee.defaultprojectkotlin

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import de.bornholdtlee.defaultprojectkotlin.injection.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

        startKoin {
            androidContext(this@BaseApplication)
            modules(repositoryModule + useCaseModule + dataModule + networkModule + helperModule)
        }
    }
}
