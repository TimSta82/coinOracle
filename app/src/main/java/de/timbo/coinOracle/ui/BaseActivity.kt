package de.timbo.coinOracle.ui

import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chuckerteam.chucker.api.Chucker
import com.google.android.material.appbar.MaterialToolbar
import com.squareup.seismic.ShakeDetector
import de.timbo.coinOracle.BuildConfig
import de.timbo.coinOracle.R

open class BaseActivity : AppCompatActivity(), ShakeDetector.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            (getSystemService(SENSOR_SERVICE) as SensorManager?)?.let { sensorManager ->
                ShakeDetector(this).also { shakeDetector -> shakeDetector.start(sensorManager) }
            }
        }
    }

    internal fun initToolbarForBackNavigation(toolbar: MaterialToolbar, titleString: String = this.javaClass.simpleName) {
        initToolbar(toolbar, titleString)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    internal fun initToolbar(toolbar: MaterialToolbar, titleString: String = this.javaClass.simpleName) {
        toolbar.title = titleString
    }

    override fun hearShake() = startActivity(Chucker.getLaunchIntent(this))
}
