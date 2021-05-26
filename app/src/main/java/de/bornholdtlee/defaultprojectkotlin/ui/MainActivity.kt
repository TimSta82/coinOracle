package de.bornholdtlee.defaultprojectkotlin.ui

import android.os.Bundle
import de.bornholdtlee.defaultprojectkotlin.databinding.ActivityMainBinding
import de.bornholdtlee.defaultprojectkotlin.utils.viewBinding

class MainActivity : BaseActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar(binding.toolbarContainer.toolbar)
    }
}
