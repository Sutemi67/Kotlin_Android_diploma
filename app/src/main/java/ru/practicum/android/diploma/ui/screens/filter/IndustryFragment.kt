package ru.practicum.android.diploma.ui.screens.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentIndustryBinding

class IndustryFragment : Fragment() {

    private val viewModel by viewModel<FilterViewModel>(ownerProducer = { requireActivity() })
    private var _binding: FragmentIndustryBinding? = null
    private val binding: FragmentIndustryBinding get() = requireNotNull(_binding)
    private var selectedIndustry = ""

    private val adapter = IndustryAdapter { industry ->
        viewModel.onSelectIndustry(industry)
        selectedIndustry = industry.name
        binding.applyButton.isVisible = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIndustryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupToolbar()
        observeIndustries()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.applyButton.setOnClickListener {
            viewModel.setWorkingArea(selectedIndustry)
            findNavController().popBackStack()
        }
    }

    private fun setupRecyclerView() {
        binding.industriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@IndustryFragment.adapter
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeIndustries() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.industries.collect { industries ->
                adapter.submitList(industries)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
