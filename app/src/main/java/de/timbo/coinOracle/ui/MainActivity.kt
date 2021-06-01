package de.timbo.coinOracle.ui

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.timbo.coinOracle.R
import de.timbo.coinOracle.databinding.ActivityMainBinding
import de.timbo.coinOracle.utils.Logger
import de.timbo.coinOracle.utils.viewBinding

class MainActivity : BaseActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val viewModel by viewModels<MainViewModel>()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.initPortfolio()
        viewModel.getEuroRate()
        setObservers()

        navController = findNavController(R.id.nav_host_fragment)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)
    }

    private fun setObservers() {
        viewModel.euroFailure.observe(this) { showError("Failed loading currency exchange rate") }
        viewModel.assetsFailure.observe(this) { showError("Failed loading assets") }
        viewModel.portfolio.observe(this) { portfolioEntity -> Logger.debug("Portfolio: $portfolioEntity") }
        viewModel.portFolioFailure.observe(this) { showError("Failed loading assets") }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
