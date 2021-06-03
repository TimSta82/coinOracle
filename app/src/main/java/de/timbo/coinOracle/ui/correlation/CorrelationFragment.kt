package de.timbo.coinOracle.ui.correlation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import de.timbo.coinOracle.R
import de.timbo.coinOracle.database.model.CorrelationEntity
import de.timbo.coinOracle.databinding.FragmentCorrelationBinding
import de.timbo.coinOracle.extensions.showSnackBar
import de.timbo.coinOracle.ui.BaseFragment
import de.timbo.coinOracle.utils.viewBinding

class CorrelationFragment : BaseFragment(R.layout.fragment_correlation) {

    private val binding by viewBinding(FragmentCorrelationBinding::bind)
    private val viewModel by viewModels<CorrelationViewModel>()
    private val correlationAdapter by lazy { CorrelationAdapter(::onClick) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
        setRecyclerView()
    }

    private fun setRecyclerView() {
        binding.correlationRv.adapter = correlationAdapter
    }

    private fun setObservers() {
        viewModel.correlations.observe(viewLifecycleOwner, ::setCorrelations)
    }

    private fun setCorrelations(correlations: List<CorrelationEntity>?) {
        correlationAdapter.submitList(correlations)
    }

    private fun onClick(correlationEntity: CorrelationEntity) {
        showSnackBar("winner: ${correlationEntity.winnerAsset.name} - loser: ${correlationEntity.loserAsset.name}")
    }
}
