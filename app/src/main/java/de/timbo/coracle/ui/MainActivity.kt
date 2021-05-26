package de.timbo.coracle.ui

import android.os.Bundle
import de.timbo.coracle.databinding.ActivityMainBinding
import de.timbo.coracle.utils.viewBinding

class MainActivity : BaseActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar(binding.toolbarContainer.toolbar)
    }
}
